FROM flink:scala_2.12-java11
RUN mkdir -p $FLINK_HOME/usrlib
COPY --chown=flink:flink target/application.jar $FLINK_HOME/usrlib/application.jar
