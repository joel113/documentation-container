FROM maven

COPY . ./src

WORKDIR /src

RUN mvn clean install

FROM openjdk

WORKDIR /src

COPY --from=0 /src/target/kafka-streams-word-count-0.0.1-SNAPSHOT.jar target/kafka-streams-word-count-0.0.1-SNAPSHOT.jar

CMD java -jar target/kafka-streams-word-count-0.0.1-SNAPSHOT.jar
