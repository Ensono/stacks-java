# Feature Specification: Spring Boot Vulnerability Mitigation Without Disabling Verification

**Feature Branch**: `001-spring-boot-vuln-mitigation`  
**Created**: 2026-03-26  
**Status**: Draft  
**Input**: User description: "Ensure Spring Boot vulnerability mitigations are applied without disabling compatibility verification and without requiring a Java upgrade"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Keep a supported startup path (Priority: P1)

As a maintainer of the Spring Boot starter, I need the application to start and load its Spring Cloud features using a supported dependency combination so that vulnerability remediation does not rely on suppressing compatibility checks.

**Why this priority**: The current workaround disables a safety check that is explicitly warning about an unsupported dependency combination. Removing that workaround safely is the core risk reduction objective.

**Independent Test**: In the `java/` module, run `./mvnw test` with the compatibility verifier left enabled by default. The application context must start without `spring.cloud.compatibility-verifier.enabled=false` in application or test config.

**Acceptance Scenarios**:

1. **Given** Spring Boot 3.5.x is provided by the parent POM, **When** the application starts on the current Java baseline, **Then** the selected Spring Cloud train and related starters pass compatibility verification without a global verifier override.
2. **Given** the application still uses Spring Cloud-backed configuration features, **When** maintainers run the standard Java module tests, **Then** the tests pass without reintroducing `spring.cloud.compatibility-verifier.enabled=false`.

---

### User Story 2 - Preserve the current Java baseline (Priority: P2)

As a delivery team constrained to the current Java runtime, I need the mitigation to work without upgrading Java so that remediation can ship within the present platform envelope.

**Why this priority**: The user has stated that a Java upgrade is not possible at this stage, so the mitigation must fit the current runtime constraints.

**Independent Test**: Validate the chosen Spring Boot and Spring Cloud combination against the current project Java baseline in the `java/` build and document that no Java version change is required.

**Acceptance Scenarios**:

1. **Given** the repository's current Java baseline, **When** dependency alignment changes are applied, **Then** no `pom.xml`, build script, or documentation change requires a Java upgrade.
2. **Given** a mitigation option would require a newer Java level, **When** alternatives are evaluated, **Then** that option is rejected and the selected approach is documented.

---

### User Story 3 - Preserve existing consumer contracts and delivery guidance (Priority: P3)

As a consumer of this starter and its pipelines, I need security-driven dependency changes to avoid unintended API, contract, or deployment behavior changes unless those changes are explicitly documented.

**Why this priority**: This repository is both runnable code and reusable starter guidance. Hidden behavioral drift would propagate to downstream teams.

**Independent Test**: Run `./mvnw verify` in `java/` and update migration documentation to confirm the change is build-time and runtime dependency alignment only, with no intended REST or pipeline contract break.

**Acceptance Scenarios**:

1. **Given** the current REST API and Swagger/OpenAPI endpoints, **When** the mitigation is implemented, **Then** no endpoint contract changes are introduced without explicit documentation.
2. **Given** the existing delivery documentation, **When** the dependency strategy changes, **Then** the migration notes describe the supported version path, rollback option, and validation steps.

### Edge Cases

- The latest Spring Cloud train compatible with Spring Boot 3.5.x may require dependency changes beyond a single BOM version bump.
- A transitive dependency used only through legacy bootstrap or config starters may keep vulnerable or unsupported components on the classpath.
- Test-only configuration may accidentally retain the verifier override even if production configuration removes it.
- A rollback may require reverting both dependency versions and documentation if a supported train exposes additional incompatibilities.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST run on the current Java baseline without introducing a Java upgrade requirement.
- **FR-002**: The system MUST remove reliance on `spring.cloud.compatibility-verifier.enabled=false` from runtime and test configuration.
- **FR-003**: The build MUST use a Spring Cloud dependency set and related starters that are supported with the Spring Boot version inherited from the current parent POM.
- **FR-004**: The implementation MUST preserve currently required Spring Cloud behavior such as configuration imports and refresh-scoped components, or explicitly replace it with an equivalent supported mechanism.
- **FR-005**: The implementation MUST evaluate and remove unnecessary Spring Cloud starters if they are not required for the supported solution.
- **FR-006**: The implementation MUST maintain existing REST, OpenAPI, Pact, pipeline, and deployment interfaces unless a documented compatibility change is explicitly approved.
- **FR-007**: The implementation MUST update migration or operator documentation to explain the supported dependency alignment, validation steps, and rollback path.
- **FR-008**: The implementation MUST validate the affected Maven quality gates for the touched module set, including formatting, automated tests, and relevant security checks.

### Security & Compliance Requirements *(mandatory)*

- No secrets, credentials, or production-only values may be added to code, tests, or planning artifacts.
- Examples, tests, and documentation must continue to use synthetic values only.
- The mitigation must strengthen, not weaken, dependency compatibility controls by keeping Spring's compatibility verifier active.
- Any production-impacting dependency or runtime behavior change must be reviewable through normal pull request and change-control processes.

### Compatibility & Migration *(mandatory when interfaces change)*

- No REST, OpenAPI, Pact, pipeline, or deployment contract changes are intended for this feature.
- The change is expected to be backward compatible for API consumers; the compatibility impact is limited to internal dependency alignment and startup validation behavior.
- Migration consists of adopting the supported Spring Cloud and starter configuration and removing the verifier override from runtime and test configuration.
- Rollback consists of reverting the dependency alignment and documentation updates in one change if a supported combination exposes a blocker during validation.

### Key Entities *(include if feature involves data)*

- **Dependency Baseline**: The effective combination of Spring Boot, Spring Cloud BOM, Spring Cloud starters, and Java level used by the application.
- **Compatibility Verification Policy**: The rule that startup must succeed with Spring's compatibility verifier enabled and without local overrides.
- **Validation Matrix**: The set of required commands and expected outcomes proving the new dependency baseline is supported.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: `spring.cloud.compatibility-verifier.enabled=false` no longer appears in application or test configuration for the Java module.
- **SC-002**: The Java module test suite passes using the supported dependency baseline while retaining the current Java version.
- **SC-003**: The chosen solution is documented with a supported Spring Boot and Spring Cloud compatibility path and at least one rejected alternative.
- **SC-004**: Required automated validation for touched modules completes without introducing new formatting, test, or security gate failures attributable to the change.
- **SC-005**: No intentional REST, OpenAPI, Pact, pipeline, or deployment contract changes are introduced without explicit documentation.

## Assumptions

- The parent POM remains the source of the Spring Boot 3.5.x version for this repository.
- The current Java baseline is acceptable for the selected Spring Boot 3.5.x-compatible Spring Cloud release train.
- Consumers of the starter expect no public API changes from this work.
- Upgrading Spring Cloud and trimming unnecessary starters is allowed within the current delivery window.
- If full vulnerability elimination requires changes outside this repository's direct dependency controls, those gaps will be documented rather than hidden by disabling verification.
