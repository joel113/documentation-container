# Java Fakeservice Example

This fake service example is derived from the example provided by open telemetry.

https://github.com/open-telemetry/opentelemetry-java

https://github.com/open-telemetry/opentelemetry-java-docs

https://github.com/open-telemetry/opentelemetry-java-docs/tree/main/prometheus

This example demonstrates how to use the OpenTelemetry SDK to instrument a
simple application using Prometheus as the metric exporter and expose the
metrics via HTTP.

These are collected by a Prometheus instance which is configured to pull these
metrics via HTTP.

# How to run

## 1 - Compile

```shell script
../gradlew shadowJar
```

## 2 - Run

Start the application and prometheus via docker compose

```shell
docker-compose up
```

## 3 - View metrics

To view metrics in prometheus, navigate to:

http://localhost:9090/graph?g0.range_input=15m&g0.expr=incoming_messages&g0.tab=0

To fetch application metrics in prometheus format, run:

curl localhost:19090/metrics

To fetch application metrics in OpenMetrics format, which includes exemplars, run:

curl -H 'Accept: application/openmetrics-text; version=1.0.0; charset=utf-8' localhost:19090/metrics
