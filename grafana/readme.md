# Grafana

## Grafana

## Prometheus

## Alertmanager

https://github.com/prometheus/alertmanager

https://hub.docker.com/r/prom/alertmanager

## Grafana Agent

I started this container project with having a look at the Grafana agent. The agent collects metrics, logs and traces and submits it to the configured targets.

### Github

https://github.com/grafana/agent

### Image

`docker pull grafana/agent:latest`

### Container

```
docker run \
  -v /tmp/agent:/etc/agent/data \
  -v /tmp/grafana-agent-config.yaml:/etc/agent/agent.yaml \
  grafana/agent:latest
```

### Configuration

The Grafana Agent has mutltiple independent subsystems including

- metrics,
- logs,
- traces and
- integrations.

Integration provides individual features which collect metrics e.g. agent or node\_exporter. Agent provides metrics from the grafana agent while node\_exporter provides metrics from the linux machine that grafana agent is running on. The following example shows a configuration setup of integrations with the agent feature enabled and a remote write to prometheus:

```
metrics:
  wal_directory: /tmp/wal
  global:
    remote_write:
      - url: http://localhost:9009/api/prom/push

integrations:
  agent:
    enabled: true
```

I did setup the config with a symbolic link to the config in the `grafana-agent` subdirectory:

`ln -s ~/src-personal/dodcumentation-containers/grafana-agent/grafana-agent-config.yaml grafana-agent-config.yaml`

### Repositories

https://github.com/grafana

https://github.com/grafana/intro-to-mlt

https://github.com/grafana/agent

### Docker Compose

This project uses a docker-compose setup to spawn a full prometheus / grafana stack. Please follow to following links for more information about docker-compose:

https://docs.docker.com/compose/compose-file/

https://stackoverflow.com/questions/38088279/communication-between-multiple-docker-compose-projects

## Grafana Agent

https://grafana.com/docs/agent/latest/configuration/create-config-file/

https://grafana.com/docs/agent/latest/configuration/metrics-config/

## Grafana

This project uses also grafana to have a full prometheus / grafana stack. Please follow to following links for grafana configuration:

https://hub.docker.com/r/grafana/grafana

https://grafana.com/docs/grafana/latest/setup-grafana/configure-docker/

https://github.com/grafana/grafana/blob/main/conf/defaults.ini

https://grafana.com/docs/grafana/latest/administration/provisioning/

https://github.com/vegasbrianc/prometheus/blob/master/grafana/provisioning/datasources/datasource.yml

## Grafana Mimir

This project uses also grafana mimir to have a full prometheus / grafana stack. Please follow to following links for grafana mimir configuration:

https://grafana.com/oss/mimir/

https://grafana.com/docs/mimir/latest/operators-guide/get-started/

https://github.com/grafana/mimir/blob/main/operations/helm/charts/mimir-distributed/values.yaml

https://grafana.com/docs/mimir/latest/operators-guide/reference-http-api/

## Grafana Loki

This project uses also grafana loki to have a full prometheus / grafana stack. Please follow to following links for grafana loki configuration:

https://grafana.com/oss/loki/

https://github.com/grafana/loki

https://hub.docker.com/r/grafana/loki

https://hub.docker.com/r/grafana/loki

https://grafana.com/docs/grafana-cloud/data-configuration/logs/collect-logs-with-agent/

https://grafana.com/docs/loki/latest/configuration/examples/#almost-zero-dependencyyaml

## Prometheus

This project uses also prometheus to have a full prometheus / grafana stack. Please follow to following links for prometheus configuration:

https://hub.docker.com/r/prom/prometheus

https://prometheus.io/docs/introduction/overview/

https://prometheus.io/docs/prometheus/latest/configuration/configuration/

https://github.com/prometheus/prometheus/blob/main/documentation/examples/prometheus.yml

https://prometheus.io/docs/instrumenting/pushing/

https://stackoverflow.com/questions/62693341/pushing-metrics-data-to-prometheus

https://github.com/prometheus/pushgateway

## References

https://grafana.com/docs/agent/next/set-up/install-agent-docker/

https://grafana.com/docs/agent/next/configuration/create-config-file/

https://grafana.com/docs/agent/next/about-agent/

https://grafana.com/docs/agent/next/api/

https://grafana.com/docs/agent/next/operator/

https://grafana.com/docs/agent/latest/configuration/create-config-file/

## Strava Datasource

https://github.com/grafana/strava-datasource
