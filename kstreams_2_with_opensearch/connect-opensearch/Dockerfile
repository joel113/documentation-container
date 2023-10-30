FROM alpine
RUN apk add git
RUN apk add gradle

WORKDIR /src
RUN git clone https://github.com/Aiven-Open/opensearch-connector-for-apache-kafka.git
WORKDIR opensearch-connector-for-apache-kafka
RUN gradle installDist

FROM confluentinc/cp-kafka-connect:7.5.1

WORKDIR /kafka-connect-plugins
COPY --from=0 /src/opensearch-connector-for-apache-kafka/build/install kafka-connect-plugins

