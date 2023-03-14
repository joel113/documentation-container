package main

import (
	"net/http"
	"joel113/go-fakeservice/fakeservice"
)

func main() {

	// create http mux
	fakeserviceMux := fakeservice.NewFakeserviceService()

	// start http server
	if err := http.ListenAndServe(":3001", fakeserviceMux); err != nil {
		panic(err)
	}
}
