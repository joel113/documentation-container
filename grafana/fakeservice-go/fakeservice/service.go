package fakeservice

import (
	"context"
	"log"
	"net/http"

	"go.opentelemetry.io/otel/metric/instrument"
	"go.opentelemetry.io/otel/sdk/metric"
)

const (
	pingRoute  		string = "/ping/"
	notfoundRoude	string = "/notfound/"
	metricsRoute   	string = "/metrics/"
)

type fakeserviceHandler struct {
	pingCounter *instrument.Int64Counter
}

// the ping function implements the http.Handler interface:
// type Handler interface {
//    ServeHTTP(ResponseWriter, *Request)
//}
func (fs fakeserviceHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	(*fs.pingCounter).Add(context.Background(), 1)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"response":"pong"}`))
	log.Print("Ping request replied with pong")
}

func NewFakeserviceService(provider *metric.MeterProvider, promHandler http.Handler) *http.ServeMux {
	meter := provider.Meter("another sample counter in service.go")
	pingCounter, err := meter.Int64Counter("foobar", instrument.WithDescription("a simple int counter of pings"))
	if err != nil {
		log.Fatal(err)
	}

	// simply add 1 because otherwise the metric does not appear in the prometheus export
	pingCounter.Add(context.Background(), 1)

	// the net/http go package differentiates between handler and muxes where
	// - a handler is a controller containing the application logic and
	// - a mux stores a mapping between predefined url paths and handlers
	// the following example of a pingRoute uses a custom handler while the 
	// notfoundRoute uses the predefined http.NotFoundHandler()
	mux := http.NewServeMux()
	fs := fakeserviceHandler{&pingCounter}
	nf := http.NotFoundHandler()
	mux.Handle(pingRoute, fs)
	mux.Handle(notfoundRoude, nf)
	mux.Handle(metricsRoute, promHandler)

	// simple logging package which indicates that we are starting the http server
	log.Print("Starting http server")
	
	return mux
}
