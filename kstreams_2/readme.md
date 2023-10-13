# Another Kafka Streams

https://github.com/confluentinc

## Kafka Event Streaming Applications Demo

https://github.com/confluentinc/cp-demo

https://github.com/confluentinc/examples

https://github.com/confluentinc/kafka-streams-examples

## Kafka Docker Images

https://github.com/confluentinc/cp-docker-images/

https://docs.confluent.io/platform/current/installation/docker/image-reference.html

## Kafka Server

https://hub.docker.com/r/confluentinc/cp-server/ - Commercial Edition

https://hub.docker.com/r/confluentinc/cp-kafka/ - Community Edition

https://docs.docker.com/compose/environment-variables/#the-env-file

https://github.com/confluentinc/kafka-tutorials/issues/461 - Github Issue discussing cp-server vs. cp-kafka

## Kafka KSQLDB

https://github.com/confluentinc/ksql

https://ksqldb.io/examples.html

https://docs.confluent.io/platform/current/ksqldb/tutorials/index.html#ksqldb-tutorials-examples

## Kafka Control Center

https://hub.docker.com/r/confluentinc/cp-enterprise-control-center - Enterprise Feature

https://hub.docker.com/r/confluentinc/cp-control-center - Deprecated

https://docs.confluent.io/platform/current/control-center/index.html

https://docs.confluent.io/platform/current/control-center/installation/install-apache-kafka.html#license-keys

## Kafka Rest - Rest Proxy

https://hub.docker.com/r/confluentinc/cp-kafka-rest

https://github.com/confluentinc/kafka-rest

## Kafka Connect Datagen - Generate Custom Test Data

https://docs.ksqldb.io/en/0.10.1-ksqldb/developer-guide/test-and-debug/generate-custom-test-dat

https://hub.docker.com/r/cnfldemos/kafka-connect-datagen/

https://github.com/confluentinc/kafka-connect-datagen#run-connector-in-docker-compose

https://github.com/confluentinc/kafka-connect-datagen/blob/master/docker-compose.yml

https://hub.docker.com/r/cnfldemos/kafka-connect-datagen/tags

```
docker-compose up -d --build
curl -X POST -H "Content-Type: application/json" --data @config/connector_pageviews.config http://localhost:8083/connectors
docker-compose exec connect kafka-console-consumer --topic pageviews --bootstrap-server kafka:29092  --property print.key=true --max-messages 5 --from-beginning
```

https://hub.docker.com/r/cnfldemos/cp-server-connect-operator-with-datagen

https://github.com/confluentinc/kafka-connect-datagen

https://github.com/confluentinc/avro-random-generator

## Kafka Cat

https://hub.docker.com/r/confluentinc/cp-kafkacat/
