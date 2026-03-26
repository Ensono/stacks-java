---

description: "Task list template for feature implementation"
---

# Tasks: [FEATURE NAME]

**Input**: Design documents from `/specs/[###-feature-name]/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Include automated test tasks for every behavior-changing story. Omit tests only when the plan explicitly shows that no executable behavior changed, such as documentation-only work.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Application module**: `java/src/main/`, `java/src/test/`, `java/pom.xml`
- **API and contract tests**: `api-tests/src/test/`, `api-tests/pom.xml`
- **Pipeline and deployment**: `build/azDevOps/azure/`, `deploy/`
- **Documentation and guidance**: `docs/`, `README.md`, `.github/`
- Paths shown below assume this repository layout - adjust based on plan.md structure

<!-- 
  ============================================================================
  IMPORTANT: The tasks below are SAMPLE TASKS for illustration purposes only.
  
  The /speckit.tasks command MUST replace these with actual tasks based on:
  - User stories from spec.md (with their priorities P1, P2, P3...)
  - Feature requirements from plan.md
  - Entities from data-model.md
  - Endpoints from contracts/
  
  Tasks MUST be organized by user story so each story can be:
  - Implemented independently
  - Tested independently
  - Delivered as an MVP increment
  
  DO NOT keep these sample tasks in the generated tasks.md file.
  ============================================================================
-->

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Confirm touched modules and directories per implementation plan
- [ ] T002 Capture required validation commands for each touched module in the plan
- [ ] T003 [P] Prepare feature scaffolding in `java/`, `api-tests/`, `build/azDevOps/azure/`, or `docs/` as applicable

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

Examples of foundational tasks (adjust based on your project):

- [ ] T004 Define shared domain, API, or pipeline contracts used by all affected stories
- [ ] T005 [P] Add or update security, authorization, or configuration scaffolding required by the feature
- [ ] T006 [P] Add or update shared test fixtures, providers, or contract definitions in `java/` or `api-tests/`
- [ ] T007 Create base models, mappings, or templates that all stories depend on
- [ ] T008 Configure logging, observability, and error handling paths affected by the change
- [ ] T009 Record compatibility, migration, or rollback notes for shared interfaces

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - [Title] (Priority: P1) 🎯 MVP

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 1

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T010 [P] [US1] Add or update unit or integration coverage in `java/src/test/` for [behavior]
- [ ] T011 [P] [US1] Add or update contract or functional API coverage in `api-tests/src/test/` for [consumer journey] when interfaces change

### Implementation for User Story 1

- [ ] T012 [P] [US1] Implement domain or DTO changes in `java/src/main/java/...`
- [ ] T013 [P] [US1] Implement controller, handler, mapper, or service changes in `java/src/main/java/...`
- [ ] T014 [US1] Update resource or configuration files in `java/src/main/resources/` if required
- [ ] T015 [US1] Update API contracts, deployment manifests, or pipeline assets if this story changes an interface
- [ ] T016 [US1] Add validation, error handling, and security-path behavior
- [ ] T017 [US1] Update story-specific documentation or migration notes if user-facing behavior changes

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently

---

## Phase 4: User Story 2 - [Title] (Priority: P2)

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 2

- [ ] T018 [P] [US2] Add or update unit or integration coverage in `java/src/test/` for [behavior]
- [ ] T019 [P] [US2] Add or update contract or functional API coverage in `api-tests/src/test/` for [consumer journey] when interfaces change

### Implementation for User Story 2

- [ ] T020 [P] [US2] Implement domain or DTO changes in `java/src/main/java/...`
- [ ] T021 [US2] Implement controller, handler, mapper, or service changes in `java/src/main/java/...`
- [ ] T022 [US2] Update contracts, manifests, or config for the story as needed
- [ ] T023 [US2] Integrate with User Story 1 components while preserving backward compatibility

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently

---

## Phase 5: User Story 3 - [Title] (Priority: P3)

**Goal**: [Brief description of what this story delivers]

**Independent Test**: [How to verify this story works on its own]

### Tests for User Story 3

- [ ] T024 [P] [US3] Add or update unit or integration coverage in `java/src/test/` for [behavior]
- [ ] T025 [P] [US3] Add or update contract or functional API coverage in `api-tests/src/test/` for [consumer journey] when interfaces change

### Implementation for User Story 3

- [ ] T026 [P] [US3] Implement domain or DTO changes in `java/src/main/java/...`
- [ ] T027 [US3] Implement controller, handler, mapper, or service changes in `java/src/main/java/...`
- [ ] T028 [US3] Update contracts, manifests, or config for the story as needed

**Checkpoint**: All user stories should now be independently functional

---

[Add more user story phases as needed, following the same pattern]

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] TXXX [P] Run formatting and required validation commands in each touched Maven module
- [ ] TXXX [P] Run additional static analysis or security scans required by the change
- [ ] TXXX [P] Update documentation in `docs/`, `README.md`, or `.github/` guidance files
- [ ] TXXX Record ADR or change-summary update for architectural or workflow changes when applicable
- [ ] TXXX Confirm rollback or migration notes are complete for interface changes
- [ ] TXXX Run quickstart or operator validation steps

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2 → P3)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) - No dependencies on other stories
- **User Story 2 (P2)**: Can start after Foundational (Phase 2) - May integrate with US1 but should be independently testable
- **User Story 3 (P3)**: Can start after Foundational (Phase 2) - May integrate with US1/US2 but should be independently testable

### Within Each User Story

- Tests for behavior changes MUST be written and FAIL before implementation
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Models within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together:
Task: "Add or update unit or integration coverage in java/src/test/ for [behavior]"
Task: "Add or update contract or functional API coverage in api-tests/src/test/ for [consumer journey]"

# Launch independent implementation tasks for User Story 1 together:
Task: "Implement domain or DTO changes in java/src/main/java/..."
Task: "Implement controller, handler, mapper, or service changes in java/src/main/java/..."
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Test User Story 1 independently
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1
   - Developer B: User Story 2
   - Developer C: User Story 3
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing behavior changes
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
