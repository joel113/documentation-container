# Prometheus

## Image

`docker pull prom/prometheus`

## Client

https://github.com/prometheus/client_java

## Data Structures

Prometheus has to types of vectors to store data and to map timestampts to the data:

* instant vectors
* range rectors

With instant vectors only a single data point is mapped to the timestamp:

```
curl 'http://localhost:9090/api/v1/query' \
  --data 'query=http_requests_total{code="200"}' \
  --data time=1608481001
{
  "metric": {"__name__": "http_requests_total", "code": "200"},
  "value": [1608481001, "881"]

```

With range vectors a every data point is mapped to a range of timestamps:

```
curl 'http://localhost:9090/api/v1/query' \
  --data 'query=http_requests_total{code="200"}[30s]' \
  --data time=1608481001
{
  "metric": {"__name__": "http_requests_total", "code": "200"},
  "values": [
    [1608480978, "863"],
    [1608480986, "874"],
    [1608480094, "881"]
  ]
}
```

An instant vector can be charted while a range vector can not without any kind of aggregation, because only single values can be charted. Furthermore, an instant vector can be compared and have arithmetic performed on them, range vectors can not.

A range vector results from appending a duration to a metric, which results in returning a range vector instead of an instant vector:

```
curl 'http://localhost:9090/api/v1/query' \
  --data 'query=increase(http_requests_total{code="200",handler="/api/v1/query"}[15m])'
{
  "metric": {"__name__": "http_requests_total", "code": "200", "handler":"/api/v1/query"},
  "values": [
    [1608437313, "18.4"]
  ]
}
```

Range vectors do support many kind of vector which can applied on the range vectors to make instant vectors of the range vectors.

References:

[1] https://satyanash.net/software/2021/01/04/understanding-prometheus-range-vectors.html
