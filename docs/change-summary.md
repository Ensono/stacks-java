# Branch `fix/security_2025-11-17` Change Summary

_Last updated: 2025-12-09_

## At-a-Glance

- Codified AI usage and security guardrails under `.github/` and added MCP client settings to make the repo ready for AI-assisted contributions.
- Modernized Cucumber/Serenity test discovery, tag management, and Azure DevOps templates, eliminating the CI failures around functional test selection.
- Raised the Java parent stack to `stacks-modules-parent 3.0.107`, aligned Spring Cloud to the 2025.0.0 train, and upgraded OWASP Dependency-Check plus pipeline credentials handling.
- Documented the Spring Boot 3.5 and Jackson BOM migration steps while addressing runtime regressions (security filter chain profiles and bean primaries).

## Detailed Breakdown & Reuse Guidance

### 1. Governance & Repo Automation

- **AI guardrails:** Added `.github/copilot-instructions.md` and `.github/copilot-security-instructions.md` to spell out architecture context plus strict security/compliance rules for AI assistants. The `.github/prompts/plan.fixCucumberTestDiscovery.prompt.md` captures the remediation plan for the Cucumber discovery issue.
  - _Applicability_: Copy these instructions (or tailored variants) into other Stacks repos to maintain consistent AI usage expectations and provide ready-made remediation playbooks.
- **VS Code MCP wiring:** `.vscode/mcp.json` now registers GitHub, Maven, and Azure DevOps MCP servers, and `.vscode/settings.json` disables auto Java build updates for calmer local dev loops.
  - _Applicability_: Reuse the MCP definition anywhere the same helper services are desired for Copilot Chat or other MCP-aware tools.
- **Dependabot triage:** `.github/dependabot.yml` now assigns Java PRs to `RichardSlater` instead of `steveclewer`.
  - _Applicability_: Update assignees per ownership changes in sibling projects to keep automation actionable.

### 2. Test Infrastructure & Azure Pipelines

- **Serenity/Cucumber modernization:** `api-tests` now carries `cucumber.filter.tags` as a Maven property, downgrades Serenity/Cucumber to the stable `4.2.26 / 7.22.2` pair, wires the filter/glue/plugin defaults via `cucumber.properties`, and converts `CucumberTestSuite` to `@ConfigurationParameter`-based configuration with `SerenityReporterParallel`. The failsafe plugin receives the computed `cucumber.filter.tags` at runtime.
  - _Applicability_: Apply the same template to any Cucumber 7.x suite in the Stacks family to remove deprecated `@SelectClasspathResource` usage and to guarantee deterministic tag filtering (especially where Serenity timelines caused CI issues).
- **Tag translation helper:** Added `build/azDevOps/azure/scripts/convert-junit-tags-to-cucumber.bash` plus a wrapper template `deploy-post-deploy-tests-cucumber.yml`. Azure DevOps now converts `Functional | Smoke | Performance` inputs into `@Functional or @Smoke ...` before invoking the shared `deploy-post-deploy-tests` template, which in turn passes tags to the updated Serenity runner (`deploy-post-deploy-tests-serenity.yml`).
  - _Applicability_: Drop the script + wrapper into any pipeline relying on the upstream Ensono template set when Cucumber tags must stay in sync with legacy JUnit syntax.
- **Pipeline wiring:** `azure-pipelines-javaspring-k8s.yml` injects a new variable group `stacks-java-owasp-keys`, replaces `deploy-post-deploy-tests.yml` calls with the cucumber-aware template, and threads the allowed/ignored tag lists into the template parameters.
  - _Applicability_: Mirror these parameter changes when other environments need fine-grained control of functional test tags or when OWASP credentials are sourced from KeyVault-backed variable groups.

### 3. Security Scanning & Dependency Management

- **Parent & BOM upgrades:** `java/pom.xml` now inherits from `com.ensono.stacks.modules:stacks-modules-parent:3.0.107`, bumps `spring.cloud.dependencies.version` to `2025.0.0`, refreshes `pitest-junit5-plugin` to `1.2.1`, and introduces `junit-platform.*` dependency management to satisfy Spring Boot 3.5 expectations. Individual Jackson module versions defer to the Jackson BOM (see new docs below).
  - _Applicability_: Any project targeting Spring Boot 3.5+ should adopt the same parent version and dependency overrides; be mindful of the groupId change (`com.ensono.*`).
- **OWASP Dependency-Check hardening:** Both `java/pom.xml` and `api-tests/pom.xml` set the plugin to `12.1.9` and expose configuration slots for the NVD API key plus OSS Index credentials. Pipeline templates (`build-java.yml`, `build-api-tests*.yml`) now run the scan inline via `./mvnw verify -P owasp-dependency-check` while injecting those secrets through the new variables. The root Azure pipeline renames `vulnerability_scan_api_key` to explicit NVD/OSS values and consumes a new variable group `stacks-java-owasp-keys`.
  - _Applicability_: Share this pattern across other repos to keep OWASP checks functional now that NVD API throttling is enforced.
- **Node coverage toolchain:** `build/azDevOps/azure/coverage/package.json` removes the `<21` upper-bound on Node 20, adds an overrides block for `inline-source`, and refreshes `package-lock.json` (large diff) accordingly.
  - _Applicability_: Use the same override when GitHub Actions or Azure builds use Node 22+, preventing `inline-source` from pulling vulnerable sub-dependencies.

### 4. Runtime Behavior Adjustments

- **Security filter chain scoping:** `ApplicationConfig` is now annotated with `@Profile("!test")` so the stricter Spring Security validation in Boot 3.5 doesn't see duplicate "match all" filter chains during tests.
  - _Applicability_: Required anywhere a project provides both "real" and "test" security configurations; without it Boot 3.5 raises `UnreachableFilterChainException`.
- **Bean primaries for inherited services:** `MenuService` gained `@Primary` to avoid `NoUniqueBeanDefinitionException` in projects with V2 service implementations that extend V1.
  - _Applicability_: Ensure whichever base service you want injected by default is marked `@Primary` once upgrading to Spring Boot 3.5.

### 5. Documentation & Developer Experience

- **Migration guides:** `docs/jackson-bom-migration.md` documents why Jackson 2.20 requires BOM import (or split properties), and `docs/spring-boot-3.5-migration.md` lists every downstream action item discovered during the upgrade (Spring Cloud matrix, security profiles, bean primaries, Maven resource filtering).
  - _Applicability_: Share directly with teams upgrading other workloads so they can preempt the same build/runtime regressions.
- **README polish:** The getting-started section now uses clearer `##` headings and consistent code blocks; Docker instructions lost the extra indentation that prevented copy/paste.
  - _Applicability_: Simple documentation uplift that can be mirrored elsewhere for consistency.

## Next Steps for Other Projects

1. Decide which repositories also need the stricter AI governance docs and MCP settings, then copy/adapt the `.github` and `.vscode` assets.
2. For every Cucumber-based test suite, apply the `cucumber.properties` convention, Serenity downgrade, and Azure pipeline tag converter to eliminate flaky discovery.
3. Schedule the parent POM + OWASP plugin upgrade (plus new secrets) so all services stay compliant with NVD/OSS credential enforcement.
4. Review each service module for overlapping security configs or inherited beans and add `@Profile`/`@Primary` annotations before moving to Spring Boot 3.5.
5. Circulate the new migration guides to platform teams so they can incorporate the lessons learned into their own upgrade runbooks.
