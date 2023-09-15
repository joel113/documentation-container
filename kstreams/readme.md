# Kafka Streams

https://github.com/confluentinc/kafka-streams-examples

## RocksDB MacOS ARM Issue

```
    <dependency>
      <groupId>org.rocksdb</groupId>
      <artifactId>rocksdbjni</artifactId>
      <version>6.22.1.1</version>
      <scope>compile</scope>
    </dependency>
```

https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams/7.1.1-ccs

https://github.com/facebook/rocksdb/issues/7720

https://github.com/apache/kafka/pull/11967

https://stackoverflow.com/questions/67087968/rocksdb-noclassdeffound-how-to-setup-rocksdb-for-kafka-streams

## Kafka

```
docker-compose -f docker-compose-cp-enterprise-kafka.yml stop

docker exec -it broker sh

docker exec -it broker sh -c "/bin/kafka-topics --create --topic streams-plaintext-input --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092"

docker exec -it broker sh -c "/bin/kafka-topics --create --topic streams-wordcount-output --partitions 1 --replication-factor 1 --bootstrap-server localhost:9092"

docker exec -it broker sh -c "/bin/kafka-console-producer --broker-list localhost:9092 --topic streams-plaintext-input"

docker exec -it broker sh -c "/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic streams-wordcount-output --from-beginning --property print.key=true --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer"
```

### Images

https://hub.docker.com/r/confluentinc/cp-kafka

https://hub.docker.com/r/confluentinc/cp-server

https://hub.docker.com/r/confluentinc/cp-enterprise-kafka

https://docs.confluent.io/platform/current/installation/docker/installation.html

### References

https://github.com/confluentinc/cp-all-in-one

https://github.com/confluentinc/cp-all-in-one/blob/7.5.0-post/cp-all-in-one-kraft/docker-compose.yml

https://github.com/confluentinc/cp-demo

https://github.com/confluentinc/examples