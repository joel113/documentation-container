package io.confluent.examples.streams.microservices;

import io.confluent.examples.streams.avro.microservices.Order;
import io.confluent.examples.streams.avro.microservices.OrderValidation;
import io.confluent.examples.streams.microservices.domain.Schemas;
import io.confluent.examples.streams.microservices.util.MicroserviceTestUtils;

import org.apache.kafka.test.TestUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static io.confluent.examples.streams.avro.microservices.OrderState.CREATED;
import static io.confluent.examples.streams.avro.microservices.OrderValidationResult.FAIL;
import static io.confluent.examples.streams.avro.microservices.OrderValidationResult.PASS;
import static io.confluent.examples.streams.avro.microservices.OrderValidationType.FRAUD_CHECK;
import static io.confluent.examples.streams.avro.microservices.Product.JUMPERS;
import static io.confluent.examples.streams.avro.microservices.Product.UNDERPANTS;
import static io.confluent.examples.streams.microservices.domain.Schemas.Topics;
import static io.confluent.examples.streams.microservices.domain.beans.OrderId.id;
import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class FraudServiceTest extends MicroserviceTestUtils {

  private FraudService fraudService;

  @BeforeClass
  public static void startKafkaCluster() throws Exception {
    if (!MicroserviceTestUtils.CLUSTER.isRunning()) {
      MicroserviceTestUtils.CLUSTER.start();
    }

    MicroserviceTestUtils.CLUSTER.createTopic(Topics.ORDERS.name());
    MicroserviceTestUtils.CLUSTER.createTopic(Topics.ORDER_VALIDATIONS.name());
    final Properties config = new Properties();
    config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MicroserviceTestUtils.CLUSTER.schemaRegistryUrl());
    Schemas.configureSerdes(config);
  }

  @After
  public void tearDown() {
    fraudService.stop();
    MicroserviceTestUtils.CLUSTER.stop();
  }

  @Test
  public void shouldValidateWhetherOrderAmountExceedsFraudLimitOverWindow() throws Exception {
    //Given
    fraudService = new FraudService();

    final List<Order> orders = Arrays.asList(
        new Order(id(0L), 0L, OrderState.CREATED, Product.UNDERPANTS, 3, 5.00d),
        new Order(id(1L), 0L, OrderState.CREATED, Product.JUMPERS, 1, 75.00d),
        new Order(id(2L), 1L, OrderState.CREATED, Product.JUMPERS, 1, 75.00d),
        new Order(id(3L), 1L, OrderState.CREATED, Product.JUMPERS, 1, 75.00d),
        new Order(id(4L), 1L, OrderState.CREATED, Product.JUMPERS, 50, 75.00d),    //Should fail as over limit
        new Order(id(5L), 2L, OrderState.CREATED, Product.UNDERPANTS, 10, 100.00d),//First should pass
        new Order(id(6L), 2L, OrderState.CREATED, Product.UNDERPANTS, 10, 100.00d),//Second should fail as rolling total by customer is over limit
        new Order(id(7L), 2L, OrderState.CREATED, Product.UNDERPANTS, 1, 5.00d)    //Third should fail as rolling total by customer is still over limit
    );
    MicroserviceTestUtils.sendOrders(orders);

    //When
    fraudService.start(MicroserviceTestUtils.CLUSTER.bootstrapServers(), TestUtils.tempDirectory().getPath(), new Properties());

    //Then there should be failures for the two orders that push customers over their limit.
    final List<OrderValidation> expected = Arrays.asList(
        new OrderValidation(id(0L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(1L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(2L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(3L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(4L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.FAIL),
        new OrderValidation(id(5L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(6L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.FAIL),
        new OrderValidation(id(7L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.FAIL)
    );
    final List<OrderValidation> read = MicroserviceTestUtils.read(Topics.ORDER_VALIDATIONS, 8, MicroserviceTestUtils.CLUSTER.bootstrapServers());
    Assertions.assertThat(read).isEqualTo(expected);
  }
}