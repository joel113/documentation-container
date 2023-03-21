FROM confluentinc/cp-kafka:7.3.0

WORKDIR usr/bin

COPY create-kafka-topics.sh create-kafka-topics.sh
USER root
RUN chmod +x create-kafka-topics.sh
ENTRYPOINT ["./create-kafka-topics.sh"]