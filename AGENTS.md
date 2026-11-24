# AGENTS.md

Spring Boot 4.2 / Java 21 service that returns a random joke from an upstream API, with a fixed fallback joke when the upstream fails. Commands and configuration are in `README.md`.

## Verify

`mvn verify` is the gate, and CI runs it on JDK 21 and 25. For a change to runtime behaviour, also run the built jar and `curl localhost:8080/quotes/`, because tests pass on things the real app gets wrong (log format, tracing, circuit breaker wiring). Before each run, confirm port 8080 is free, since a stale process serves old code and looks like a pass.

## Gotchas

- **Circuit breaker needs `spring-boot-starter-aspectj`.** Without it `@CircuitBreaker` is silently ignored and failures return 500 instead of the fallback. `QuoteServiceImplTest` uses the full context so it goes red if the starter is removed.
- **Boot 4 split its modules.** Test slices and builders live in new packages (`org.springframework.boot.webmvc.test.autoconfigure`, `org.springframework.boot.restclient.test`). Tracing needs `spring-boot-micrometer-tracing-brave` besides the Brave bridge, or `traceId` vanishes from the logs. `TracingConfigurationTest` covers this.
- **Upstream HTTP goes through `RestClient`**, with timeouts set by the ordered-first `RestClientCustomizer` in `RestClientConfig`. Tests bind `MockServerRestClientCustomizer`, which is unordered so it applies last and its mock factory wins. Keep that order.
- **Lombok is wired by `annotationProcessorPaths`** in `pom.xml`, which JDK 23+ requires. Leave the version to the Boot BOM.
- **Mockito runs as a `-javaagent`** through the surefire `argLine` and `maven-dependency-plugin:properties`. Keep both together.
- **`Quote.fallback` is `@JsonIgnore`.** It reaches clients only as the `X-Fallback: true` header, set in `QuoteController`.
- **Config prefix is `quote.*`**, bound by `QuoteProperties`. A new property also goes in `META-INF/additional-spring-configuration-metadata.json` and the README table.

## Workflow

- One change per branch and PR. Merge with a merge commit (`gh pr merge N --merge`), then delete the branch locally and on origin.
- `master` requires the checks `Build and test (JDK 21)` and `Build and test (JDK 25)`. Renaming the matrix or changing the Java versions means updating branch protection first, or PRs cannot merge.
- Dependabot opens weekly PRs. Spring Boot and Spring Cloud arrive grouped, because each Cloud release targets one Boot version. For a major bump of a library without test coverage (logging, for instance), run the real jar before merging.
