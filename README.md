# Quote service

[![CI](https://github.com/thomasma/quote-service/actions/workflows/ci.yml/badge.svg)](https://github.com/thomasma/quote-service/actions/workflows/ci.yml)

A Spring Boot REST service that returns a random joke. It fetches the joke from [api.chucknorris.io](https://api.chucknorris.io) and answers with a fixed fallback joke when that API is unavailable.

Requires Java 21 or later and Maven.

## Run

    mvn clean spring-boot:run

or build and run the jar:

    mvn package
    java -jar target/quote-services-0.1.0.jar

The service listens on port 8080. Set the `PORT` environment variable to use another port.

## Usage

    curl -i http://localhost:8080/quotes/

```json
{"type":"success","value":{"id":"CpM7NFI7Qeq1ublS1WuMaw","joke":"Chuck Norris can clip his toenails, ..."}}
```

### Fallback

If the joke API is unreachable, slow, or returns an error, the service still answers `200` with a fixed joke (id `1`).
The body has the same shape as a real one. The only difference is the response header `X-Fallback: true`, which real jokes do not have:

    HTTP/1.1 200
    X-Fallback: true

The upstream call has a 2s connect timeout and a 3s read timeout. A circuit breaker watches the last 10 calls: once at least 5 have been made and half of them failed, it stops calling the joke API for 5s and serves the fallback directly. It then lets 3 trial calls through before closing again.

## Configuration

| Property | Environment variable | Default |
|---|---|---|
| `server.port` | `PORT` | `8080` |
| `quote.service-uri` | `QUOTE_SERVICEURI` | `https://api.chucknorris.io` |
| `quote.service-path` | `QUOTE_SERVICEPATH` | `/jokes/random` |
| `quote.connect-timeout` | `QUOTE_CONNECTTIMEOUT` | `2s` |
| `quote.read-timeout` | `QUOTE_READTIMEOUT` | `3s` |

Environment variable names follow Spring's relaxed binding, which drops the dashes (`service-uri` becomes `QUOTE_SERVICEURI`).
Timeouts take a unit such as `500ms` or `3s`.

## Observability

Logs are JSON, one object per line, and carry the `traceId` of the request.

Actuator endpoints:

    curl http://localhost:8080/actuator/health
    curl http://localhost:8080/actuator/info
    curl http://localhost:8080/actuator/metrics
    curl http://localhost:8080/actuator/prometheus

`prometheus.yml` is a sample Prometheus configuration that scrapes the service on `localhost:8080`.

## Tests

    mvn test

CI runs `mvn verify` on JDK 21 and 25.

## Contributing

Each change goes in its own pull request. `AGENTS.md` lists the project's gotchas and workflow, for people as well as agents. There is no license file yet.
