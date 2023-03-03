FROM debian

RUN apt update

RUN apt install -y default-jre
RUN apt install -y software-properties-common curl gnupg

RUN curl -sq http://ksqldb-packages.s3.amazonaws.com/deb/0.28/archive.key | apt-key add -

RUN add-apt-repository "deb http://ksqldb-packages.s3.amazonaws.com/deb/0.28 stable main"
RUN apt update

RUN apt install confluent-ksqldb
