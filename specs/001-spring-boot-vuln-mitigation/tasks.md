# Tasks: Spring Boot Vulnerability Mitigation Without Disabling Verification

**Input**: Design documents from `/specs/001-spring-boot-vuln-mitigation/`
**Prerequisites**: `plan.md`, `spec.md`, `research.md`, `data-model.md`, `quickstart.md`, `contracts/runtime-compatibility.md`

**Tests**: Include automated test tasks for each behavior-changing story. This feature changes startup compatibility, dependency alignment, and supported runtime behavior, so Java module test coverage and build validation are required.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Confirm the shared implementation surface and validation flow before dependency changes begin.

- [X] T001 Confirm the shared implementation surface in `specs/001-spring-boot-vuln-mitigation/plan.md`
- [X] T002 [P] Capture the validation matrix and rollback checks in `specs/001-spring-boot-vuln-mitigation/quickstart.md`
- [X] T003 [P] Capture the runtime compatibility rules in `specs/001-spring-boot-vuln-mitigation/contracts/runtime-compatibility.md`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Define the supported dependency baseline and shared migration guidance that all user stories depend on.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete.

- [X] T004 Define the supported Spring Boot and Spring Cloud baseline in `specs/001-spring-boot-vuln-mitigation/research.md`
- [X] T005 [P] Update the dependency baseline entity and validation rules in `specs/001-spring-boot-vuln-mitigation/data-model.md`
- [X] T006 [P] Prepare shared migration and rollback guidance in `docs/spring-boot-3.5-migration.md`

**Checkpoint**: The supported baseline, rollback path, and shared validation rules are documented and ready for implementation.

---

## Phase 3: User Story 1 - Keep a supported startup path (Priority: P1) 🎯 MVP

**Goal**: Align the Java module to a supported Spring Boot and Spring Cloud combination and remove the compatibility verifier workaround while preserving required Spring Cloud behavior.

**Independent Test**: Run `cd java && ./mvnw test` and confirm the application context starts without `spring.cloud.compatibility-verifier.enabled=false` in `java/src/main/resources/application.yml` or `java/src/test/resources/application-test.yml`.

### Tests for User Story 1

- [X] T007 [P] [US1] Extend startup and secrets endpoint regression coverage in `java/src/test/java/com/amido/stacks/workloads/menu/api/v1/SecretsControllerTest.java`

### Implementation for User Story 1

- [X] T008 [US1] Align the Spring Cloud BOM and retained Spring Cloud starters in `java/pom.xml`
- [X] T009 [P] [US1] Remove the compatibility verifier override from `java/src/main/resources/application.yml`
- [X] T010 [P] [US1] Remove the compatibility verifier override while preserving test-only Cloud settings in `java/src/test/resources/application-test.yml`
- [X] T011 [US1] Update retained refresh-scope or configuration behavior in `java/src/main/java/com/amido/stacks/workloads/menu/service/v1/SecretsService.java` if required.

**Checkpoint**: User Story 1 is complete when the Java module starts under test with verifier enforcement intact and the existing secrets behavior still works.

---

## Phase 4: User Story 2 - Preserve the current Java baseline (Priority: P2)

**Goal**: Ensure the supported dependency alignment works on the existing Java baseline and prevent accidental drift to a higher Java requirement.

**Independent Test**: Run `cd java && ./mvnw test` and `cd java && ./mvnw verify` using the current Java runtime, and confirm the updated build configuration and documentation do not require a Java upgrade.

### Tests for User Story 2

- [X] T012 [P] [US2] Execute `./mvnw test` and `./mvnw -Dgpg.skip=true verify` in `java/` to confirm the current Java 17 baseline is sufficient for the new dependency alignment and that no Java upgrade is required by the build or tests.

### Implementation for User Story 2

- [X] T013 [US2] Finalize Java-17-compatible dependency selections and exclusions in `java/pom.xml`
- [X] T014 [P] [US2] Record the no-Java-upgrade decision and rejected alternatives in `specs/001-spring-boot-vuln-mitigation/research.md`
- [X] T015 [P] [US2] Update Java baseline validation steps in `specs/001-spring-boot-vuln-mitigation/quickstart.md`

**Checkpoint**: User Story 2 is complete when the supported dependency baseline is enforced and documented to remain on the current Java level.

---

## Phase 5: User Story 3 - Preserve existing consumer contracts and delivery guidance (Priority: P3)

**Goal**: Prove that the mitigation does not introduce unintended API or pipeline contract changes and publish the final migration guidance for downstream consumers.

**Independent Test**: Run `cd java && ./mvnw verify` and confirm `/v1/secrets` behavior remains unchanged while migration documentation explains the supported dependency path and rollback option.

### Tests for User Story 3

- [X] T016 [P] [US3] Add regression coverage for unchanged `/v1/secrets` behavior in `java/src/test/java/com/amido/stacks/workloads/menu/api/v1/SecretsControllerTest.java`

### Implementation for User Story 3

- [X] T017 [US3] Publish the supported dependency path and rollback guidance in `docs/spring-boot-3.5-migration.md`
- [X] T018 [P] [US3] Update the final runtime compatibility evidence in `specs/001-spring-boot-vuln-mitigation/contracts/runtime-compatibility.md`
- [X] T019 [P] [US3] Record the no-contract-change outcome in `specs/001-spring-boot-vuln-mitigation/quickstart.md`

**Checkpoint**: User Story 3 is complete when consumer-facing guidance is updated and regression coverage shows the existing API behavior remains stable.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validate the final change set across formatting, tests, verification, and security scanning.

- [X] T020 Run formatting in `java/` with `./mvnw fmt:format`
- [X] T021 Run the Java module tests in `java/` with `./mvnw test`
- [X] T022 Run full Java module verification in `java/` with `./mvnw -Dgpg.skip=true verify`
- [X] T023 Run dependency security validation in `java/` with `./mvnw -Dgpg.skip=true -DautoUpdate=false -DassemblyAnalyzerEnabled=false -P owasp-dependency-check verify`
- [X] T024 Confirm the final validation and rollback notes in `docs/spring-boot-3.5-migration.md`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1: Setup**: No dependencies, start immediately.
- **Phase 2: Foundational**: Depends on Phase 1 and blocks all user stories.
- **Phase 3: User Story 1**: Depends on Phase 2 and delivers the MVP.
- **Phase 4: User Story 2**: Depends on Phase 2 and can proceed after or alongside User Story 1 if the team chooses, but it should be validated against the aligned dependency baseline.
- **Phase 5: User Story 3**: Depends on Phase 2 and can proceed after or alongside User Story 1 once the dependency baseline is stable enough to document.
- **Phase 6: Polish**: Depends on all desired user stories being complete.

### User Story Dependencies

- **US1**: No dependency on other stories after the foundational phase.
- **US2**: Depends on the supported dependency baseline selected in US1-related implementation but remains independently testable as a Java baseline guarantee.
- **US3**: Depends on the supported dependency baseline selected in US1-related implementation but remains independently testable as a documentation and regression proof story.

### Within Each User Story

- Write or update tests first for the story's runtime or regression behavior.
- Complete dependency or configuration changes before final story documentation.
- Validate the story independently before moving to lower-priority work.

### Parallel Opportunities

- `T002` and `T003` can run in parallel during setup.
- `T005` and `T006` can run in parallel during the foundational phase.
- `T009` and `T010` can run in parallel after `T008` begins because they touch separate configuration files.
- `T014` and `T015` can run in parallel after the Java baseline enforcement approach is chosen.
- `T018` and `T019` can run in parallel while consumer-facing guidance is finalized.
- `T020` through `T023` are parallelizable execution tasks, subject to machine capacity and whether earlier commands need to complete first in the chosen workflow.

---

## Parallel Example: User Story 1

```bash
# After selecting the supported Spring Cloud train in java/pom.xml:
Task: "Remove the compatibility verifier override from java/src/main/resources/application.yml"
Task: "Remove the compatibility verifier override while preserving test-only Cloud settings in java/src/test/resources/application-test.yml"
```

## Parallel Example: User Story 2

```bash
# After adding the Java baseline guard in java/pom.xml:
Task: "Record the no-Java-upgrade decision and rejected alternatives in specs/001-spring-boot-vuln-mitigation/research.md"
Task: "Update Java baseline validation steps in specs/001-spring-boot-vuln-mitigation/quickstart.md"
```

## Parallel Example: User Story 3

```bash
# While final documentation is being completed:
Task: "Update the final runtime compatibility evidence in specs/001-spring-boot-vuln-mitigation/contracts/runtime-compatibility.md"
Task: "Record the no-contract-change outcome in specs/001-spring-boot-vuln-mitigation/quickstart.md"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup.
2. Complete Phase 2: Foundational.
3. Complete Phase 3: User Story 1.
4. Validate `cd java && ./mvnw test` before moving on.

### Incremental Delivery

1. Deliver the supported startup path in US1.
2. Add Java baseline enforcement and documentation in US2.
3. Finish consumer-facing guidance and regression proof in US3.
4. Run the full validation and security pass in Phase 6.

### Suggested MVP Scope

- User Story 1 only, because it removes the unsupported verifier bypass and establishes a supported runtime baseline.

## Notes

- All tasks use the required checklist format: checkbox, task ID, optional `[P]`, optional `[US#]`, and exact file path.
- Tests are explicitly included for each behavior-changing story.
- No REST, OpenAPI, Pact, pipeline, or deployment contract changes are planned for this feature.
