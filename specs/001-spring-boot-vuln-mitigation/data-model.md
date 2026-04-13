# Data Model: Spring Boot Vulnerability Mitigation Without Disabling Verification

## Entity: Dependency Baseline

- **Description**: The effective supported runtime stack for the Java module.
- **Fields**:
  - `springBootSource`: parent POM version source controlling Spring Boot.
  - `springCloudTrain`: selected Spring Cloud BOM release train.
    - `cloudStarters`: list of retained Spring Cloud starters or narrower replacements.
  - `javaBaseline`: current Java level that must remain unchanged.
  - `overridePropertiesRemoved`: list of verifier or legacy compatibility overrides removed from configuration.
- **Validation Rules**:
  - `springCloudTrain` must be compatible with the effective Spring Boot version.
    - `springCloudTrain` is `2025.0.1` for the implemented repository baseline.
  - `javaBaseline` must not increase as part of this feature.
  - `overridePropertiesRemoved` must include the compatibility verifier override if present in current configuration.

## Entity: Compatibility Verification Policy

- **Description**: The runtime rule set that defines acceptable startup behavior.
- **Fields**:
  - `verifierEnabled`: boolean, expected to remain true by default.
  - `supportedCombination`: supported Spring Boot and Spring Cloud pairing.
  - `requiredCloudCapabilities`: configuration import, refresh scope, and any other retained Cloud behavior.
  - `forbiddenWorkarounds`: list of disallowed properties or startup bypasses.
- **Validation Rules**:
  - `verifierEnabled` must stay true in normal runtime and test configuration.
  - `forbiddenWorkarounds` must include `spring.cloud.compatibility-verifier.enabled=false`.
  - `requiredCloudCapabilities` must be preserved or replaced with an explicitly documented equivalent.

## Entity: Validation Matrix

- **Description**: The set of commands and expected outcomes proving the dependency baseline is acceptable.
- **Fields**:
  - `module`: affected module, primarily `java`.
  - `commands`: ordered validation commands.
  - `expectedOutcome`: pass criteria for each command.
  - `rollbackTrigger`: condition that would require reverting the dependency alignment.
- **Validation Rules**:
  - The matrix must include formatting, test, and verify coverage for `java`.
  - Security-oriented dependency verification must be included when dependency versions are changed.
  - A rollback trigger must be documented for any blocking incompatibility discovered after alignment.

## Relationships

- `Dependency Baseline` must satisfy the `Compatibility Verification Policy`.
- `Validation Matrix` proves the `Dependency Baseline` conforms to the policy.

## State Transitions

- `Proposed` -> `Aligned`: supported dependency train and starters are selected.
- `Aligned` -> `Validated`: required quality and security gates pass with verifier enabled.
- `Validated` -> `Documented`: migration notes and rollback guidance are updated.
