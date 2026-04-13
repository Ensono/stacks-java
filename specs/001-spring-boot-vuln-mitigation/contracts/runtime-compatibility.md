# Runtime Compatibility Contract

## Intent

Define the non-API contract for this feature: the application must run on the current Java baseline with a supported Spring Boot and Spring Cloud combination while keeping compatibility verification active.

## Contract Rules

1. The Java module must not require a Java version increase to implement this feature.
2. The effective Spring Cloud train must be one that Spring documents as compatible with the inherited Spring Boot 3.5.x line. In this repository, that train is `spring-cloud-dependencies` `2025.0.1`.
3. `spring.cloud.compatibility-verifier.enabled=false` must not be present in `java/src/main/resources/application.yml` or `java/src/test/resources/application-test.yml`.
4. Any retained Spring Cloud dependency must be justified by a runtime capability still used by the application. In this repository, `spring-cloud-context` is retained for `@RefreshScope` support.
5. The existing REST, OpenAPI, Pact, pipeline, and deployment interfaces are unchanged unless a separate compatibility note is added.

## Verification Evidence

- `java/pom.xml` shows the supported dependency alignment.
- Java module tests and verification succeed without a verifier override.
- Migration documentation explains the supported path and rollback option.
