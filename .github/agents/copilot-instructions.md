# stacks-java Development Guidelines

Auto-generated from all feature plans. Last updated: 2026-03-26

## Active Technologies

- Java 17 baseline with Spring Boot 3.5.x inherited from `stacks-modules-parent:3.0.139`, Spring Cloud BOM aligned to a Boot 3.5.x-supported train, Stacks core modules, JUnit 5, Maven Wrapper, Azure DevOps pipeline automation (001-spring-boot-vuln-mitigation)

## Project Structure

```text
java/
api-tests/
build/
deploy/
docs/
specs/
```

## Commands

- `cd java && ./mvnw fmt:format`
- `cd java && ./mvnw test`
- `cd java && ./mvnw -Dgpg.skip=true verify`
- `cd api-tests && ./mvnw test`

## Code Style

Use the domain-driven package layout under `java/src/main/java`, keep application code in `java/`, functional API validation in `api-tests/`, and delivery automation under `build/azDevOps/azure/` and `deploy/`.

## Recent Changes

- 001-spring-boot-vuln-mitigation: aligned Spring Boot 3.5.x support to Spring Cloud 2025.0.1, removed compatibility-verifier overrides, retained `spring-cloud-context`, and documented the validation path.

<!-- MANUAL ADDITIONS START -->
<!-- MANUAL ADDITIONS END -->
