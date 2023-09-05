package io.confluent.examples.streams.microservices;

import static io.confluent.examples.streams.avro.microservices.OrderState.CREATED;
import static io.confluent.examples.streams.avro.microservices.OrderState.FAILED;
import static io.confluent.examples.streams.avro.microservices.OrderState.VALIDATED;
import static io.confluent.examples.streams.avro.microservices.Product.JUMPERS;
import static io.confluent.examples.streams.avro.microservices.Product.UNDERPANTS;
import static io.confluent.examples.streams.microservices.domain.beans.OrderId.id;
import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import io.confluent.examples.streams.avro.microservices.Order;
import io.confluent.examples.streams.avro.microservices.OrderValidation;
import io.confluent.examples.streams.avro.microservices.OrderValidationResult;
import io.confluent.examples.streams.avro.microservices.OrderValidationType;
import io.confluent.examples.streams.microservices.domain.Schemas;
import io.confluent.examples.streams.microservices.domain.Schemas.Topics;
import io.confluent.examples.streams.microservices.util.MicroserviceTestUtils;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.test.TestUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidationsAggregatorServiceTest extends MicroserviceTestUtils {

  private List<Order> orders;
  private List<OrderValidation> ruleResults;
  private ValidationsAggregatorService ordersService;


  @BeforeClass
  public static void startKafkaCluster() throws InterruptedException {
    MicroserviceTestUtils.CLUSTER.createTopic(Topics.ORDERS.name());
    MicroserviceTestUtils.CLUSTER.createTopic(Topics.ORDER_VALIDATIONS.name());
    final Properties config = new Properties();
    config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, MicroserviceTestUtils.CLUSTER.schemaRegistryUrl());
    Schemas.configureSerdes(config);
    MicroserviceTestUtils.tailAllTopicsToConsole(MicroserviceTestUtils.CLUSTER.bootstrapServers());
  }

  @Test
  public void shouldAggregateRuleSuccesses() throws Exception {

    //Given
    ordersService = new ValidationsAggregatorService();

    orders = Arrays.asList(
        new Order(id(0L), 0L, OrderState.CREATED, Product.UNDERPANTS, 3, 5.00d),
        new Order(id(1L), 0L, OrderState.CREATED, Product.JUMPERS, 1, 75.00d)
    );
    MicroserviceTestUtils.sendOrders(orders);

    ruleResults = Arrays.asList(
        new OrderValidation(id(0L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(0L), OrderValidationType.ORDER_DETAILS_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(0L), OrderValidationType.INVENTORY_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(1L), OrderValidationType.FRAUD_CHECK, OrderValidationResult.PASS),
        new OrderValidation(id(1L), OrderValidationType.ORDER_DETAILS_CHECK, OrderValidationResult.FAIL),
        new OrderValidation(id(1L), OrderValidationType.INVENTORY_CHECK, OrderValidationResult.PASS)
    );
    MicroserviceTestUtils.sendOrderValuations(ruleResults);

    //When
    ordersService.start(MicroserviceTestUtils.CLUSTER.bootstrapServers(), TestUtils.tempDirectory().getPath(), new Properties());

    //Then
    final List<KeyValue<String, Order>> finalOrders = MicroserviceTestUtils
        .readKeyValues(Topics.ORDERS, 4, MicroserviceTestUtils.CLUSTER.bootstrapServers());
    Assertions.assertThat(finalOrders.size()).isEqualTo(4);

    //And the first order should have been validated but the second should have failed
    Assertions.assertThat(finalOrders.stream().map(kv -> kv.value).collect(Collectors.toList())).contains(
        new Order(id(0L), 0L, OrderState.VALIDATED, Product.UNDERPANTS, 3, 5.00d),
        new Order(id(1L), 0L, OrderState.FAILED, Product.JUMPERS, 1, 75.00d)
    );
  }

  @After
  public void tearDown() {
    ordersService.stop();
  }
}
