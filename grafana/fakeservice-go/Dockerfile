FROM golang:1.19

WORKDIR /app

COPY . /app

RUN go mod tidy
RUN go build -o /fakeservice

EXPOSE 3001

CMD [ "/fakeservice" ]
