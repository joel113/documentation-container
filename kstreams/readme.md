# Kafka Streams

Kafka Streams provides real-time streaming on top of the Kafka Consumer Client.

Kafka Streams provides stateless (map, filter, etc.) and stateful transformations (aggregations, joins, and windowing).

https://github.com/apache/kafka/tree/trunk

https://github.com/apache/kafka/tree/trunk/streams

https://github.com/confluentinc/kafka-streams-examples

https://www.baeldung.com/java-kafka-streams-vs-kafka-consumer

## KStream

KStream handles the stream of records.

https://github.com/apache/kafka/blob/trunk/streams/src/main/java/org/apache/kafka/streams/kstream/KStream.java

Read and deserialize a stream:

```
StreamsBuilder builder = new StreamsBuilder();
KStream<String, String> textLines = 
  builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
```

Stateless transformations:

```
KStream<String, String> textLinesUpperCase =
  textLines
    .map((key, value) -> KeyValue.pair(value, value.toUpperCase()))
    .filter((key, value) -> value.contains("FILTER"));
```

## KTable

KTable manages the changelog stream with the latest state of a given key. Each data record represents an update.

GlobalKTables can be used to broadcast information to all tasks or to do joins without re-partition the input data.

https://github.com/apache/kafka/blob/trunk/streams/src/main/java/org/apache/kafka/streams/kstream/KTable.java

https://developer.confluent.io/courses/kafka-streams/ktable/

Read a table:

```
KTable<String, String> textLinesTable = 
  builder.table(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
```

Read a global table:

```
GlobalKTable<String, String> textLinesGlobalTable = 
  builder.globalTable(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
```

Stateful transformations:

```
KTable<String, Long> wordCounts = textLines
.flatMapValues(value -> Arrays.asList(value
.toLowerCase(Locale.getDefault()).split("\\W+")))
.groupBy((key, word) -> word)
.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>> as("counts-store"));
```

Joining two streams into one stream with 5s windowing grouped by key:

```
KStream<String, String> leftRightSource = leftSource.outerJoin(rightSource,
  (leftValue, rightValue) -> "left=" + leftValue + ", right=" + rightValue,
    JoinWindows.of(Duration.ofSeconds(5))).groupByKey()
      .reduce(((key, lastValue) -> lastValue))
  .toStream();
```

## Exactly-Once Processing

```
streamsConfiguration.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG,
StreamsConfig.EXACTLY_ONCE);
``

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