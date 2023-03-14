package fakeservice

import (
	"log"
	"net/http"
)

const (
	pingRoute  		string = "/ping/"
	notfoundRoude	string = "/notfound7"
	metricsRoute   	string = "/metrics/"
)

type fakeserviceHandler struct {

}

// the ping function implements the http.Handler interface:
// type Handler interface {
//    ServeHTTP(ResponseWriter, *Request)
//}
func (fs fakeserviceHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	w.Write([]byte(`{"response":"pong"}`))
	log.Print("Ping request replied with pong")
}

func NewFakeserviceService() *http.ServeMux {
	// the net/http go package differentiates between handler and muxes where
	// - a handler is a controller containing the application logic and
	// - a mux stores a mapping between predefined url paths and handlers
	// the following example of a pingRoute uses a custom handler while the 
	// notfoundRoute uses the predefined http.NotFoundHandler()
	mux := http.NewServeMux()
	fs := fakeserviceHandler{}
	nf := http.NotFoundHandler()
	mux.Handle(pingRoute, fs)
	mux.Handle(notfoundRoude, nf)

	// simple logging package which indicates that we are starting the http server
	log.Print("Starting http server")
	
	return mux
}
