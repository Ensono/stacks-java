# Implementation Plan: Spring Boot Vulnerability Mitigation Without Disabling Verification

**Branch**: `001-spring-boot-vuln-mitigation` | **Date**: 2026-03-26 | **Spec**: `/specs/001-spring-boot-vuln-mitigation/spec.md`
**Input**: Feature specification from `/specs/001-spring-boot-vuln-mitigation/spec.md`

**Note**: This plan resolves the current unsupported Spring Boot and Spring Cloud combination by moving to a supported dependency baseline that keeps the compatibility verifier enabled and does not require a Java upgrade.

## Summary

Align the Java module's Spring dependency stack to a supported Spring Boot 3.5.x-compatible Spring Cloud release train, remove the `spring.cloud.compatibility-verifier.enabled=false` workaround from runtime and test configuration, and document the supported path and rollback steps. Audit the existing Spring Cloud starters so the application retains required configuration and refresh behavior without carrying unnecessary legacy starters.

## Technical Context

**Language/Version**: Java 17 baseline with Spring Boot 3.5.x inherited from `stacks-modules-parent:3.0.139`  
**Primary Dependencies**: Spring Boot starter web and actuator, Spring Cloud BOM aligned to a Boot 3.5.x-supported train, `spring-cloud-starter-config` or narrower replacement if supported, `spring-cloud-starter-bootstrap` only if still required, Stacks core modules, JUnit 5  
**Storage**: N/A for this feature; existing runtime profile-based configuration and optional secrets imports remain in place  
**Testing**: `cd java && ./mvnw fmt:format`, `cd java && ./mvnw test`, `cd java && ./mvnw verify`, `cd java && ./mvnw -P owasp-dependency-check verify` as relevant to touched dependencies  
**Target Platform**: Linux container runtime, local development, and Azure DevOps pipeline validation  
**Project Type**: Spring Boot REST API starter  
**Performance Goals**: No material regression to startup or existing request handling; startup must succeed with compatibility verification enabled  
**Constraints**: No Java upgrade, no verifier disablement, no sensitive data, preserve API and pipeline compatibility, stay within supported Spring release trains  
**Scale/Scope**: Repository-scoped dependency alignment for the `java/` module with supporting documentation updates for downstream starter consumers

## Constitution Check

*GATE: Passed before Phase 0 research. Re-checked after Phase 1 design and still passed.*

- [x] Security and compliance impact reviewed: no secrets or sensitive data added; change strengthens dependency safety by keeping the verifier active.
- [x] Test strategy defined first: Java module formatting, test, verify, and dependency/security validation are identified before implementation.
- [x] Compatibility impact captured: no intended REST, contract, pipeline, or deployment interface change; migration and rollback notes are documented.
- [x] Quality gates listed per touched module: `fmt:format`, `test`, `verify`, and OWASP dependency-check for `java/`.
- [x] Documentation impact captured: migration notes and dependency guidance updates are included.
- [x] Any constitution exception is justified in Complexity Tracking with reviewer-visible rationale.

## Project Structure

### Documentation (this feature)

```text
specs/001-spring-boot-vuln-mitigation/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── runtime-compatibility.md
└── tasks.md
```

### Source Code (repository root)

```text
java/
├── pom.xml
├── src/main/resources/
├── src/test/resources/
└── src/main/java/

docs/
└── spring-boot-3.5-migration.md
```

**Structure Decision**: The change is centered on `java/pom.xml` for dependency alignment, `java/src/main/resources/` and `java/src/test/resources/` for removing the verifier override, and `docs/` for migration guidance. `java/src/main/java/` may be touched only if dependency trimming requires replacing a Spring Cloud feature with a supported equivalent. Validation is concentrated in the `java/` module because no API contract change is intended.

## Phase 0: Research Summary

- Adopt a Spring Cloud release train that officially supports Spring Boot 3.5.x so the compatibility verifier can remain enabled.
- Prefer removing unnecessary Spring Cloud starters, especially legacy bootstrap support, if the supported configuration can preserve required behavior without them.
- Reject mitigations that either keep the verifier disabled, downgrade away from the targeted Spring Boot line without explicit security justification, or require a Java upgrade.

## Phase 1: Design Summary

- Model the change around a `Dependency Baseline`, `Compatibility Verification Policy`, and `Validation Matrix` so the implementation can separate version alignment from validation and rollback concerns.
- Treat runtime compatibility as the primary contract for this feature: the application must start under the current Java baseline with verifier enforcement intact and without public API changes.
- Provide a quickstart focused on validating the supported dependency baseline and confirming the verifier override is gone.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|--------------------------------------|
| None      | N/A        | N/A                                  |
