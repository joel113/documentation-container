## Grafana Alloy

This example highlights setting up the Grafana Alloy in a simple tracing pipeline.

1. Start up the stack.

```bash
docker compose up -d
```

At this point, the following containers should be spun up:

```bash
docker compose ps
```

```bash
NAME                 IMAGE                                       COMMAND                  SERVICE      CREATED          STATUS         PORTS
grafana_alloy_for_vscode-alloy-1        grafana/alloy:v1.16.1    "/bin/alloy run /etc…"   alloy        5 hours ago   Up 4 hours   0.0.0.0:4317-4318->4317-4318/tcp, [::]:4317-4318->4317-4318/tcp, 0.0.0.0:12345->12345/tcp, [::]:12345->12345/tcp
grafana_alloy_for_vscode-grafana-1      grafana/grafana:12.4     "/run.sh"                grafana      5 hours ago   Up 5 hours   0.0.0.0:3000->3000/tcp, [::]:3000->3000/tcp
grafana_alloy_for_vscode-loki-1         grafana/loki:3.3.0       "/usr/bin/loki -conf…"   loki         5 hours ago   Up 5 hours   0.0.0.0:3100->3100/tcp, [::]:3100->3100/tcp
grafana_alloy_for_vscode-prometheus-1   prom/prometheus:latest   "/bin/prometheus --c…"   prometheus   5 hours ago   Up 5 hours   0.0.0.0:9090->9090/tcp, [::]:9090->9090/tcp
grafana_alloy_for_vscode-tempo-1        grafana/tempo:latest     "/tempo -config.file…"   tempo        5 hours ago   Up 5 hours   0.0.0.0:55848->3200/tcp, [::]:55848->3200/tcp, 0.0.0.0:55847->4317/tcp, [::]:55847->4317/tcp, 0.0.0.0:55846->4318/tcp, [::]:55846->4318/tcp
```

2. If you're interested you can see the wal/blocks as they are being created.

```bash
ls tempo-data/
```

3. Navigate to [Grafana](http://localhost:3000/explore) select the Tempo data source and use the "Search"
tab to find traces.

4. To stop the setup use:

```console
docker compose down -v
```

### VSCode Github Metrics

https://code.visualstudio.com/updates/v1_119

https://code.visualstudio.com/docs/copilot/guides/monitoring-agents

### Grafana Alloy

https://grafana.com/docs/alloy/latest/

### Grafana Tempo

https://grafana.com/docs/tempo/latest/docker-example/

https://github.com/grafana/tempo/tree/main/example/docker-compose/single-binary

### Grafana Images

https://hub.docker.com/r/grafana/grafana/

### Grafana OpenTelemetry Collector

https://grafana.com/docs/opentelemetry/collector/opentelemetry-collector/

### Github Copilot Dashboard

https://grafana.com/grafana/dashboards/25053-github-copilot/

### TraceQL Queries

https://grafana.com/docs/tempo/latest/metrics-from-traces/metrics-queries/

### Github COpilot Models and Pricing

https://docs.github.com/en/copilot/reference/copilot-billing/models-and-pricing
