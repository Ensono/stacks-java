# Stacks Pipeline Templates – Modernization Plan

Goal: make the shared `stacks-pipeline-templates` package work out-of-the-box with current Spring Boot, JUnit Platform, Serenity, and Cucumber versions, avoiding brittle discovery/tag checks and keeping reports clean.

## 1. Test Discovery & Tag Handling

- Add a Cucumber-aware tag converter in the templates (reuse `convert-junit-tags-to-cucumber.bash` logic) so `-Dgroups` inputs become `-Dcucumber.filter.tags` before running suites.
- Set `junit.platform.suite.allowEmptySuites=true` for the untagged-test check jobs to prevent `NoTestsDiscoveredException` from failing the step.
- Document the recommended suite pattern (`@Suite + @IncludeEngines("cucumber") + FEATURES_PROPERTY_NAME`) and the need for `cucumber.properties` defaults (features, glue, plugin, publish quiet, parallel) in consuming repos.
- Make the untagged-test check pass `-Duntagged.test.check=true` explicitly to ensure cleanup hooks run in consumer projects.

## 2. Ungagged / Invalid Tag Guardrail

- Update `test-maven-post-deploy-untagged-test-check.bash` to:
  - Pass `-Duntagged.test.check=true`.
  - Accept either `-Dgroups` (legacy) or `-Dcucumber.filter.tags` (preferred) and normalize to Cucumber syntax.
  - Treat empty/zero-test discovery as success and only fail when report directories contain entries.
- Provide a small JUnit Platform listener snippet in docs for consumers who need cleanup hooks (delete `target/site/serenity` and `target/failsafe-reports` when untagged check is active).

## 3. Serenity/Cucumber/JUnit Alignment

- Pin template defaults to a vetted trio (e.g., Serenity 4.3.x, Cucumber 7.33.x, JUnit Platform 1.11.x/5.11.x) and publish a compatibility matrix in the repo.
- Add guidance to avoid deprecated selectors (`@SelectClasspathResource`) and prefer `@ConfigurationParameter` with `FEATURES_PROPERTY_NAME`.
- Ensure parallel execution flags are set via `cucumber.properties` (`cucumber.execution.parallel.enabled=true`) rather than command-line flags.

## 4. Maven/Failsafe/Surefire Defaults

- In template steps, prefer `failsafe:integration-test`/`verify` with `-Dcucumber.filter.tags` instead of `-Dgroups`.
- Provide a template snippet for setting `junit.platform.properties` / `junit-platform.properties` (service loader for LauncherSessionListener, allowEmptySuites, parallelism if needed).
- Expose `maven.repo.local` parameter in all steps (already present) and document cache sizing for large Serenity runs.

## 5. Reporting & Cleanup

- After untagged-test check, always remove `target/site/serenity` and `target/failsafe-reports` (already done) but make removal tolerant of missing paths and empty dirs.
- Add an option to skip Serenity aggregation in the untagged check (saves time when zero tests run).
- Publish the expected `find` check logic in README so consumers know how to keep directories empty when the guardrail is active.

## 6. CI/CD Template Refinements

- Thread NVD/OSS credentials into OWASP Dependency-Check steps (pattern used in stacks-java) and document required variable groups.
- Add variables for allowed/ignored Cucumber tags and default them to `Functional | Smoke | Performance` and `Ignore` respectively.
- Provide a sample pipeline fragment showing how to call the untagged-test check with the new tag converter.

## 7. Documentation & Migration Notes

- Add a “Test Runner Compatibility” doc to `stacks-pipeline-templates` covering: supported Serenity/Cucumber/JUnit versions, required suite annotations, `cucumber.properties` template, and how to enable the untagged guardrail.
- Add a “Breaking Changes” section for teams upgrading from older templates (notably the tag handling shift and explicit `-Duntagged.test.check=true`).

## 8. Validation Matrix

- CI jobs in the templates repo should exercise:
  - Default tag run (positive tags) on a sample suite.
  - Untagged-test check (negated tags) confirming zero tests + empty reports.
  - Parallel execution on a small feature set.
  - Mixed-path repos (features in classpath) to validate discovery with `FEATURES_PROPERTY_NAME`.

## 9. Rollout Guidance

- Ship a minor version bump with the new defaults and docs.
- Provide an upgrade checklist for consumers: update template version, add `cucumber.properties`, adopt suite annotation pattern, optionally add the cleanup listener.
