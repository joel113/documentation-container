# Flink Docker Documentation

`docker run -it --rm flink`

`docker run -it --rm flink:latest jobmanager`

`docker exec -it 320fdc11237e /bin/sh`

## Build and run the flink application

```
docker build --rm \
  --build-arg SOME_ARGUMENT=1.14.5 \
  -t flink-application .

docker run -it --name flink --rm flink-application jobmanager

docker exec -it flink /bin/sh
```

## Build and run the flink application within a flink network

```
docker run \
    --rm \
    --name=jobmanager \
    --network flink-network \
    --publish 8081:8081 \
    --env FLINK_PROPERTIES="${FLINK_PROPERTIES}" \
    flink-application jobmanager

docker run \
    --rm \
    --name=taskmanager \
    --network flink-network \
    --env FLINK_PROPERTIES="${FLINK_PROPERTIES}" \
    flink-application taskmanager

bin/flink run -c com.joel.flink.StreamPipeline usrlib/flink-application-1.0.0-SNAPSHOT-shaded.jar 
```

## Directories

```
$FLINK_HOME/conf
$FLINK_HOME/lib
```

## References

https://hub.docker.com/_/flink

https://github.com/apache/flink-docker/blob/7d39879cd5596989f856dbd75ce9804829c22b2e/1.15/scala_2.12-java8-debian/Dockerfile

https://nightlies.apache.org/flink/flink-docs-master/docs/deployment/resource-providers/standalone/docker/
