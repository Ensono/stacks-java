# Quickstart: Spring Boot Vulnerability Mitigation Without Disabling Verification

## Goal

Validate that the Java module uses a supported Spring Boot and Spring Cloud combination, keeps the compatibility verifier enabled, and does not require a Java upgrade.

## Preconditions

- Use the repository's current Java baseline.
- Work from the feature context `001-spring-boot-vuln-mitigation`.
- Ensure the Java module dependency changes and documentation updates are present.

## Validation Steps

1. Confirm the verifier workaround is gone from the Java module configuration.
   - Check `java/src/main/resources/application.yml`.
   - Check `java/src/test/resources/application-test.yml`.
   - Expected result: neither file contains `spring.cloud.compatibility-verifier.enabled=false`.

2. Confirm the Spring Cloud train is aligned with Spring Boot 3.5.x.
   - Check `java/pom.xml`.
   - Expected result: the Spring Cloud BOM uses `2025.0.1` and the Java module retains `spring-cloud-context` instead of legacy bootstrap/config starters.

3. Run formatting for the Java module.

   ```bash
   cd java
   ./mvnw fmt:format
   ```

4. Run the Java module tests.

   ```bash
   cd java
   ./mvnw test
   ```

5. Run broader Java module verification.

   ```bash
   cd java
   ./mvnw -Dgpg.skip=true verify
   ```

   Verify the build is running on Java 17. The module now enforces `[17,18)` through Maven Enforcer, so the build should fail fast on any other runtime.

6. Run dependency/security validation if dependency versions changed.

   ```bash
   cd java
   ./mvnw -Dgpg.skip=true -DautoUpdate=false -DassemblyAnalyzerEnabled=false -P owasp-dependency-check verify
   ```

   If local signing is configured for release artifacts, keep `-Dgpg.skip=true` for developer validation so GPG does not block functional verification. If the local machine does not have the .NET 8 runtime installed, disable the Java-irrelevant Assembly Analyzer with `-DassemblyAnalyzerEnabled=false`.

## Expected Outcome

- The application context starts during tests without disabling the compatibility verifier.
- The Java runtime baseline remains unchanged.
- No intentional REST, OpenAPI, Pact, pipeline, or deployment contract changes are introduced.
- The Java module build enforces Java 17 and does not require a runtime upgrade beyond the existing baseline.

## Rollback

- Revert the dependency alignment, starter adjustments, and documentation updates together if a supported combination introduces a blocker that cannot be resolved in-scope.
