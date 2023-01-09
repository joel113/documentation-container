# Grafana Agent

## Image

`docker pull grafana/agent:latest`

## Container

```
docker run \
  -v /tmp/agent:/etc/agent/data \
  -v /tmp/grafana-agent-config.yaml:/etc/agent/agent.yaml \
  grafana/agent:latest
```

## Configuration

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

## References

https://grafana.com/docs/agent/next/set-up/install-agent-docker/

https://grafana.com/docs/agent/next/configuration/create-config-file/
