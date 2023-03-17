package main

import (
	"context"
	"joel113/go-fakeservice/fakeservice"
	"log"
	"net/http"

	"github.com/prometheus/client_golang/prometheus/promhttp"

	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/prometheus"
	"go.opentelemetry.io/otel/metric/instrument"
	"go.opentelemetry.io/otel/sdk/metric"
)

func main() {
	ctx := context.Background()

	exporter, err := prometheus.New()
	if err != nil {
		log.Fatal(err)
	}

	provider := metric.NewMeterProvider(metric.WithReader(exporter))
	meter := provider.Meter("a sample counter in main.go")

	attrs := []attribute.KeyValue{
		attribute.Key("A").String("B"),
		attribute.Key("C").String("D"),
	}

	counter, err := meter.Float64Counter("foo", instrument.WithDescription("a simple float counter"))
	if err != nil {
		log.Fatal(err)
	}
	counter.Add(ctx, 5, attrs...)

	// create http mux
	fakeserviceMux := fakeservice.NewFakeserviceService(provider, promhttp.Handler())

	// start http server
	if err := http.ListenAndServe(":3001", fakeserviceMux); err != nil {
		panic(err)
	}
}
