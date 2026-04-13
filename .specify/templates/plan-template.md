# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]
**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/plan-template.md` for the execution workflow.

## Summary

[Extract from feature spec: primary requirement + technical approach from research]

## Technical Context

<!--
  ACTION REQUIRED: Replace the content in this section with the concrete details
  for the feature. Defaults below are repository-oriented prompts, not final text.
-->

**Language/Version**: [Java 17, Spring Boot 3.x, or NEEDS CLARIFICATION]  
**Primary Dependencies**: [Spring Boot starters, Stacks modules, JUnit 5, AssertJ, Mockito, Pact, Serenity, or NEEDS CLARIFICATION]  
**Storage**: [N/A, in-memory, Cosmos DB, DynamoDB, or NEEDS CLARIFICATION]  
**Testing**: [./mvnw test, ./mvnw verify, api-tests Maven suite, or NEEDS CLARIFICATION]  
**Target Platform**: [Linux container, Kubernetes, Azure DevOps pipeline, or NEEDS CLARIFICATION]  
**Project Type**: [Spring Boot REST API starter, pipeline automation, or NEEDS CLARIFICATION]  
**Performance Goals**: [feature-specific SLOs, throughput, latency, or NEEDS CLARIFICATION]  
**Constraints**: [security controls, compatibility limits, branch protection, no sensitive data, or NEEDS CLARIFICATION]  
**Scale/Scope**: [consumer services, starter template reach, deployment environments, or NEEDS CLARIFICATION]

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [ ] Security and compliance impact reviewed: authN/authZ, secrets, synthetic data, and change-control implications are documented.
- [ ] Test strategy defined first: unit, integration, contract, and security validation coverage is identified for each behavior change.
- [ ] Compatibility impact captured: API, contract, pipeline, and deployment interface changes include migration or rollback notes.
- [ ] Quality gates listed per touched module: `fmt:format`, `test`/`verify`, and any relevant `checkstyle`, `spotbugs`, `pitest`, `jacoco`, or OWASP checks.
- [ ] Documentation impact captured: updates to `docs/`, `README.md`, runbooks, or ADR-equivalent records are identified.
- [ ] Any constitution exception is justified in Complexity Tracking with reviewer-visible rationale.

## Project Structure

### Documentation (this feature)

```text
specs/[###-feature]/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for the feature. Remove paths that are not touched by the change.
-->

```text
java/
├── pom.xml
├── src/main/java/
├── src/main/resources/
└── src/test/java/

api-tests/
├── pom.xml
└── src/test/

build/azDevOps/azure/
deploy/
docs/
```

**Structure Decision**: [Document the selected directories, why they are touched,
and which modules require validation]

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation                  | Why Needed         | Simpler Alternative Rejected Because |
|----------------------------|--------------------|--------------------------------------|
| [e.g., 4th project]        | [current need]     | [why 3 projects insufficient]        |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient]  |
