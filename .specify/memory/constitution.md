<!--
Sync Impact Report
- Version change: 0.0.0 -> 1.0.0
- Modified principles:
	- Template Principle 1 -> I. Security and Compliance First
	- Template Principle 2 -> II. Testable Change Before Merge
	- Template Principle 3 -> III. Contract and Version Discipline
	- Template Principle 4 -> IV. Enforced Quality Gates
	- Template Principle 5 -> V. Documentation and Traceability
- Added sections:
	- Engineering Standards
	- Delivery Workflow
- Removed sections:
	- None
- Templates requiring updates:
	- updated .specify/templates/plan-template.md
	- updated .specify/templates/spec-template.md
	- updated .specify/templates/tasks-template.md
	- updated .github/prompts/address-pr-comments.prompt.md
	- updated .github/agents/speckit.tasks.agent.md
- Follow-up TODOs:
	- None
-->
# Stacks Java Constitution

## Core Principles

### I. Security and Compliance First

All changes MUST preserve authentication, authorization, branch protection, signed-commit,
and change-control safeguards. Contributors MUST use synthetic or pseudonymized data in code,
tests, documentation, and prompts; secrets, credentials, and production-only values MUST NOT be
hard-coded, copied into the repository, or sent to external LLMs. Production configuration or
infrastructure changes MUST flow through the approved pull request and change-management process.
Rationale: this repository is used in regulated client delivery, so bypasses create
disproportionate operational and compliance risk.

### II. Testable Change Before Merge

Every behavior-changing change MUST begin with automated test intent: add or update unit,
integration, or contract tests before implementation, and confirm the new test path fails before
the fix is completed when practical. API contract changes MUST update provider or consumer
coverage, and security-sensitive changes MUST validate protected and failure paths, not only the
happy path. A pull request is not complete until the relevant Maven module tests and verification
steps for affected behavior pass. Rationale: the repository already separates application and API
test modules; using them deliberately is the fastest way to prevent regressions.

### III. Contract and Version Discipline

Public interfaces MUST remain explicit and versioned. Changes to REST endpoints, OpenAPI output,
Pact contracts, pipeline inputs or outputs, or deployment manifests MUST document compatibility
impact and provide a migration or rollback path before merge. Breaking changes MUST be called out
in the spec, plan, tasks, and pull request description, and they require explicit reviewer
approval. Rationale: this starter is consumed as a template and integrated into pipelines, so
hidden interface drift propagates quickly.

### IV. Enforced Quality Gates

Every touched module MUST pass its applicable quality gates before review is considered complete.
For Maven modules this includes formatting, tests, and any relevant static analysis or security
checks such as checkstyle, SpotBugs, PITest, JaCoCo, and OWASP Dependency-Check. Changes to build,
pipeline, or coverage tooling MUST run the matching validation commands for those assets as well.
No change may rely on manual overrides of failing checks without a documented exception in the plan
and pull request. Rationale: repository quality is enforced by automation, not reviewer optimism.

### V. Documentation and Traceability

Changes that affect architecture, delivery workflow, security posture, or operator behavior MUST
update the corresponding documentation in docs/, README.md, or module-level guidance in the same
change. Long-lived architectural or governance decisions SHOULD be recorded as an ADR or equivalent
decision record when they change team defaults. Pull requests MUST describe what changed, why, how
it was validated, and how it can be rolled back if the impact is non-trivial. Rationale: this
repository acts as both product code and reusable starter guidance, so undocumented decisions cause
repeat confusion.

## Engineering Standards

- The default implementation stack is Java 17, Spring Boot, Maven Wrapper, and Azure DevOps
  pipeline automation.
- Source changes SHOULD follow the existing domain-driven package layout under
  java/src/main/java and keep feature logic grouped by aggregate or workload area, not by generic
  technical layer.
- Application code lives in java/; contract and functional API validation lives in api-tests/;
  delivery automation lives under build/azDevOps/azure/ and deploy/.
- Observability, security, and configuration changes MUST preserve existing health, Swagger or
  OpenAPI, logging, and environment-driven configuration behavior unless the spec and migration
  notes say otherwise.
- Formatting MUST use the repository's enforced formatter in every touched Maven module before
  review or merge.

## Delivery Workflow

- Work MUST be performed on a feature or fix branch; protected branches are updated through
  reviewed pull requests only.
- Commits and pull requests MUST satisfy repository security guidance, including GPG signing
  requirements where configured.
- Plans MUST include a constitution check covering security impact, test impact, contract impact,
  required quality gates, and documentation updates.
- Tasks MUST be organized by independently testable user stories, but they MUST also include shared
  work for validation, security, compatibility, and documentation when those concerns are affected.
- Reviewers MUST reject changes that weaken required controls, skip mandated validation, or omit
  compatibility or rollback notes for risky changes.

## Governance

This constitution overrides conflicting local habits, prompt snippets, and ad hoc workflow
shortcuts. Amendments MUST be made in the same repository change as any dependent template or
guidance updates they require. Versioning follows semantic versioning for governance: MAJOR for
incompatible principle or governance changes, MINOR for new principles or materially expanded
obligations, PATCH for clarifications that do not change expected behavior. Compliance with this
constitution MUST be checked during planning, implementation task generation, and pull request
review. Every constitution amendment MUST record its sync impact at the top of this file and update
the Last Amended date. The ratification date reflects the first substantive adoption of this
constitution for this repository.

**Version**: 1.0.0 | **Ratified**: 2026-03-26 | **Last Amended**: 2026-03-26
