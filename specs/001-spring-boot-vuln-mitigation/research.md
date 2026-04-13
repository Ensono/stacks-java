# Research: Spring Boot Vulnerability Mitigation Without Disabling Verification

## Decision 1: Use a Spring Cloud release train that supports Spring Boot 3.5.x

- **Decision**: Move the Java module from Spring Cloud `2024.0.3` to `2025.0.1`, the latest supported `2025.0.x` release train that matches Spring Boot 3.5.x.
- **Rationale**: Spring's published compatibility matrix maps Spring Boot 3.5.x to Spring Cloud 2025.0.x. This keeps the compatibility verifier enabled and addresses the root cause instead of suppressing the safety check.
- **Alternatives considered**:
  - Keep `2024.0.x` and disable the verifier: rejected because it knowingly runs an unsupported combination.
  - Downgrade Spring Boot to 3.4.x: rejected for this feature because the objective is to mitigate vulnerabilities while staying on the current Spring Boot line unless a documented blocker forces retreat.
  - Upgrade Java: rejected because the user stated this is not possible at this stage.

## Decision 2: Audit and trim Spring Cloud starters instead of carrying legacy bootstrap by default

- **Decision**: Remove `spring-cloud-starter-bootstrap` and replace `spring-cloud-starter-config` with `spring-cloud-context`, preserving refresh scope without carrying unused Config Client and bootstrap behavior.
- **Rationale**: The repository uses `@RefreshScope` and `spring.config.import`, but there is no clear evidence of active Config Server usage. `spring-cloud-context` provides the retained refresh-scope capability while reducing unsupported surface area and transitive dependency noise.
- **Alternatives considered**:
  - Leave both `spring-cloud-starter-bootstrap` and `spring-cloud-starter-config` unchanged: rejected because it preserves legacy surface area without proving the application needs both.
  - Remove all Spring Cloud starters: rejected because the code currently depends on `RefreshScope` and Cloud-backed configuration features.

## Decision 2a: Enforce the existing Java baseline in the Java module build

- **Decision**: Add a Maven Enforcer rule requiring Java 17 for the `java/` module.
- **Rationale**: Spring Boot 3.5.x already requires Java 17, and an explicit enforcer rule prevents accidental drift while proving no Java upgrade is required for this feature.
- **Alternatives considered**:
  - Rely on implicit parent POM defaults only: rejected because the feature explicitly promises no Java upgrade and should make that constraint executable.

## Decision 3: Validate the change through Java module quality and security gates

- **Decision**: Use the Java module as the primary validation surface and run formatting, tests, verify, and dependency/security checks after the dependency baseline is updated.
- **Rationale**: The feature changes dependency alignment and startup behavior, not public API logic. The Java module owns the effective classpath, application context startup, and runtime configuration behavior that the verifier protects.
- **Alternatives considered**:
  - Validate with unit tests only: rejected because dependency and startup compatibility issues often appear only during broader build verification.
  - Skip security-oriented dependency validation because the change is version-only: rejected because the feature exists specifically to mitigate vulnerability risk.

## Decision 4: Treat verifier enablement as a runtime contract

- **Decision**: Encode the expected outcome as a runtime compatibility contract: the application must start and tests must pass without `spring.cloud.compatibility-verifier.enabled=false` in main or test configuration.
- **Rationale**: This makes the protection explicit and reviewable, and prevents future regression to the current workaround.
- **Alternatives considered**:
  - Document the property as an accepted local exception: rejected because it normalizes an unsupported runtime state.
