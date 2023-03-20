package main

import (
	"joel113/go-fakeservice/fakeservice"
	"log"
	"net/http"

	"github.com/prometheus/client_golang/prometheus/promhttp"

	"go.opentelemetry.io/otel/exporters/prometheus"
	"go.opentelemetry.io/otel/sdk/metric"
)

func main() {

	exporter, err := prometheus.New()
	if err != nil {
		log.Fatal(err)
	}

	provider := metric.NewMeterProvider(metric.WithReader(exporter))

	// create http mux
	fakeserviceMux := fakeservice.NewFakeserviceService(provider, promhttp.Handler())

	// start http server
	if err := http.ListenAndServe(":3001", fakeserviceMux); err != nil {
		panic(err)
	}
}
