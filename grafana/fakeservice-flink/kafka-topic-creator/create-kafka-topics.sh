#!/bin/sh

# Simply wait until original kafka container and zookeeper are started.
sleep 15.0s

# Parse string of kafka topics into an array
# https://stackoverflow.com/a/10586169/4587961
kafkatopicsArrayString="$KAFKA_TOPICS"
IFS=' ' read -r -a kafkaTopicsArray <<< "$kafkatopicsArrayString"

# Create kafka topic for each topic item from split array of topics.
for newTopic in "${kafkaTopicsArray[@]}"; do
  # https://kafka.apache.org/quickstart
  kafka-topics --create --topic "$newTopic" --partitions 1 --replication-factor 1 --if-not-exists --bootstrap-server flink-broker:9092
done