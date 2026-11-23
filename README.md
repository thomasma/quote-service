# Spring Boot App that retrieves a random quote service RESTful service

*thanks to jokes from https://api.chucknorris.io*

Requires Java 21 or later.

## Run

    mvn clean spring-boot:run

The service listens on port 8080. Set the `PORT` environment variable to use another port.

If the joke API is unreachable, slow, or returns an error, the service answers with a fixed fallback joke instead.
A circuit breaker stops calling the joke API after repeated failures.

A fallback response has the same JSON body as a real one but carries the response header `X-Fallback: true`.
Real jokes do not have the header.

## Configuration

| Property | Environment variable | Default |
|---|---|---|
| `server.port` | `PORT` | `8080` |
| `quote.service-uri` | `QUOTE_SERVICEURI` | `https://api.chucknorris.io` |
| `quote.service-path` | `QUOTE_SERVICEPATH` | `/jokes/random` |
| `quote.connect-timeout` | `QUOTE_CONNECTTIMEOUT` | `2s` |
| `quote.read-timeout` | `QUOTE_READTIMEOUT` | `3s` |

### Get random quote

    curl -XGET http://localhost:8080/quotes/

## Actuator endpoints

    curl http://localhost:8080/actuator/health
    curl http://localhost:8080/actuator/info
    curl http://localhost:8080/actuator/metrics
    curl http://localhost:8080/actuator/prometheus

## Tests

    mvn test
