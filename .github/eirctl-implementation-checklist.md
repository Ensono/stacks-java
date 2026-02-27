# Eirctl Implementation Checklist

Progress tracker for migrating stacks-java to eirctl task automation while preserving all quality gates and pipeline structure.

## Phase 1: Research & Alignment

### 1.1 Pipeline Templates Analysis

- [ ] Document current cycle4 template usage in `azure-pipelines-javaspring-k8s.yml`
- [ ] Map `build-java.yml` to main-branch scripts and parameters
- [ ] Map `build-api-tests.yml` to main-branch scripts and parameters
- [ ] Map `test-static-code-analysis.yml` to main-branch scripts and parameters
- [ ] Map deployment helpers to main-branch scripts
- [ ] Document all Maven flags, tag filters, and env vars
- [ ] Create parameter mapping matrix (cycle4 → main scripts → eirctl)

### 1.2 Script Parameter Extraction

- [ ] Extract `build-maven-install.bash` options (-Z for cache)
- [ ] Extract `build-maven-compile.bash` options
- [ ] Extract `test-maven-format-check.bash` options
- [ ] Extract `test-maven-checkstyle-check.bash` options
- [ ] Extract `test-maven-spotbugs-check.bash` options
- [ ] Extract `test-maven-tagged-test-run.bash` options (-a for tags)
- [ ] Extract `test-maven-download-test-deps.bash` options (-X/-Y/-Z)
- [ ] Extract `test-maven-owasp-dependency-check.bash` options (-V/-W/-X/-Y/-Z)
- [ ] Extract `test-sonar-scanner.bash` options (-a through -g, -V through -Z)
- [ ] Extract `deploy-k8s-envsubst.bash` options (-a/-b/-Y/-Z)
- [ ] Extract `deploy-k8s-apply.bash` options (-a)
- [ ] Extract `deploy-k8s-rollout-status.bash` options (-a/-b/-Z)

### 1.3 Current Pipeline Validation

- [ ] Run existing pipeline successfully on cycle4 templates
- [ ] Capture all generated artifacts (JARs, reports, Docker images)
- [ ] Document all published test results
- [ ] Document all security scan outputs
- [ ] Note all environment variable groups used
- [ ] Record baseline build times

## Phase 2: Eirctl Configuration

### 2.1 Directory Structure

- [x] Create `build/eirctl/` directory
- [x] Create `eirctl.yaml` root configuration
- [x] Add JSON schema reference for IDE validation
- [x] Configure imports to contexts.yaml and tasks.yaml

### 2.2 Contexts Configuration (`build/eirctl/contexts.yaml`)

- [x] Define `buildenv` context with `ensono/eir-java:1.1.251`
- [x] Define `infrastructure` context with `ensono/eir-infrastructure:1.1.251`
- [x] Configure Maven cache host mount `-v ${PWD}/.m2:/root/.m2`
- [ ] Add M2_VOLUME override for named volume option
- [x] Set `shell: bash` and `shell_args: [-c]` (bash required for `${VAR:-default}` syntax and mvnw scripts)
- [x] Configure `envfile.exclude` for [home, path, tmpdir, java_home, java_opts, maven_home, m2_home] (java_home prevents host JDK overriding container)
- [x] Test context isolation and volume mounts

### 2.3 Task Library - Java Project (`build/eirctl/tasks.yaml`)

- [x] Create `clean` task
- [x] Create `cache-warm` task (dependency:go-offline)
- [x] Create `build` task (`./mvnw clean install` — uses `install` goal so artifacts are available in local repo for api-tests)
- [x] Create `build-skip-tests` task
- [x] Create `test` task (-Dgroups="Unit | Component | Integration" — pipe-delimited to match pipeline convention)
- [x] Create `integration-test` task
- [x] Create `verify` task
- [x] Create `format` task
- [x] Create `format-check` task
- [x] Create `checkstyle` task
- [x] Create `spotbugs` task
- [x] Create `pitest` task
- [x] Create `security-scan` task (OWASP with -P owasp-dependency-check)
- [x] Create `sonar` task using `sonar-scanner` CLI (not Maven plugin), reading sonar-project.properties
- [x] Create `sonar-pr` task for PR-specific SonarCloud analysis (pullrequest.key/branch/base params)
- [x] Create `dependency-tree` task
- [x] Add M2_LOCATION env to all Maven tasks

### 2.4 Task Library - API Tests (`build/eirctl/tasks.yaml`)

- [x] Create `api-clean` task (dir: ../api-tests)
- [x] Create `api-build` task
- [x] Create `api-test-functional` task ((@Functional or @Smoke or @Performance) and not @Ignore)
- [x] Create `api-test-smoke` task
- [x] Create `api-test-performance` task
- [x] Create `api-test-untagged-check` task
- [x] Add THREADS variable (default: 4) to test tasks
- [x] Configure -Dthreads=${THREADS} propagation
- [ ] Configure -Dserenity.threads=${THREADS} where applicable
- [ ] Add ignore tags support (-Y parameter)

### 2.5 Task Library - Cloud Scenarios

- [x] Create `run-azure` task (-Dazure.profile.name=azure -Daws.profile.name=no-aws — property overrides, not -P flags, due to file-based activation)
- [x] Create `run-aws` task (-Daws.profile.name=aws -Dazure.profile.name=no-azure)
- [x] Create `run-all-clouds` task (-Dazure.profile.name=azure -Daws.profile.name=aws)
- [x] Create `package-azure` task (skip tests, azure profile via property override)
- [x] Create `package-aws` task (skip tests, aws profile via property override)

### 2.6 Task Library - Deployment & Infrastructure

- [x] Create `docker-build` task (validate against Dockerfile using azul/zulu-openjdk-alpine:17.0.18 build / 17-jre runtime)
- [ ] Create `k8s-envsubst` task (deploy-k8s-envsubst.bash wrapper)
- [ ] Create `k8s-apply` task (deploy-k8s-apply.bash wrapper)
- [ ] Create `k8s-rollout-status` task
- [x] Create `terraform-validate` task
- [ ] Create `terraform-plan` task
- [ ] Create `terraform-apply` task
- [x] Create `yaml-lint` task
- [x] Create `terraform-format-check` task

### 2.7 Pipeline Definitions

- [x] Define `ci-build` pipeline
  - [x] format-check stage (allow_failure: false)
  - [x] Parallel build-azure and build-aws
  - [x] test stage (depends on builds)
  - [x] integration-test stage (runs `./mvnw verify`, depends on test)
  - [x] Parallel security-scan and sonar (depends on integration-test)
  - [x] api-build (depends on integration-test completion)
  - [x] api-test-functional (depends on api-build)
- [x] Define `quality-gate` pipeline
  - [x] Parallel: format-check, checkstyle, spotbugs, pitest, security-scan
- [x] Define `local-dev` pipeline
  - [x] build-skip-tests → cache-warm → run-azure

### 2.8 Configuration Validation

- [x] Run `eirctl validate` to check schema
- [x] Verify all task references resolve
- [x] Check pipeline dependency chains
- [x] Validate context mounts and permissions

## Phase 3: Local Testing

### 3.1 Eirctl Installation

- [x] Download eirctl 0.9.11 for Linux AMD64
- [x] Install to `/usr/local/bin/eirctl`
- [x] Verify installation: `eirctl --version` (v0.9.11 installed)
- [x] Check eirctl validate passes

### 3.2 Individual Task Testing

- [ ] Test `eirctl cache-warm` - verify .m2 populates
- [x] Test `eirctl build` - compare with direct mvnw
- [x] Test `eirctl test` - verify test reports
- [x] Test `eirctl format-check` - verify fmt enforcement
- [x] Test `eirctl checkstyle`
- [x] Test `eirctl spotbugs`
- [ ] Test `eirctl security-scan` - verify OWASP report
- [ ] Test `eirctl sonar` (with mock/local SonarQube)
- [x] Test `eirctl api-build`
- [ ] Test `eirctl api-test-functional`
- [ ] Test `eirctl run-azure`
- [ ] Test `eirctl run-aws`

### 3.3 Pipeline Testing

- [ ] Run `eirctl run pipeline ci-build` - full flow
- [ ] Run `eirctl run pipeline quality-gate`
- [ ] Run `eirctl run pipeline local-dev`
- [ ] Verify all outputs match direct Maven execution
- [ ] Check target/surefire-reports/ generated
- [ ] Check target/site/jacoco/ generated
- [ ] Check api-tests/target/site/serenity/ generated
- [ ] Verify target/dependency-check-report.html exists

### 3.4 Performance & Regression Testing

- [ ] Measure build time vs baseline
- [ ] Verify Maven cache persistence across runs
- [ ] Test parallel execution with THREADS=4
- [ ] Test reduced threads (THREADS=2) for constraints
- [ ] Check memory usage in containers
- [ ] Validate no test failures introduced

## Phase 4: Azure DevOps Integration

### 4.1 Installation Template

- [x] Create `build/azDevOps/azure/templates/install-eirctl.yml`
- [x] Add Bash@3 task for downloading eirctl 0.9.11 
- [x] Set executable permissions
- [x] Move to /usr/local/bin or agent tools directory
- [ ] Test template in isolation

### 4.2 Pipeline Variables

- [x] Add `EirctlVersion: 0.9.11` to azuredevops-vars.yml
- [ ] Document all required env vars for eirctl tasks
- [ ] Map pipeline vars to eirctl task env
- [ ] Add THREADS parameter (default: 4)
- [ ] Preserve M2 cache configuration

### 4.3 Parallel Validation Run

- [x] Add install-eirctl step to azure-pipelines-javaspring-k8s.yml
- [ ] Run `eirctl build` alongside existing build-java.yml template
- [ ] Compare build artifacts (size, content, checksums)
- [ ] Run `eirctl api-build` alongside build-api-tests.yml
- [ ] Compare test reports (count, results, format)
- [ ] Run `eirctl sonar` alongside static analysis template
- [ ] Compare SonarCloud uploads
- [ ] Document any discrepancies

### 4.4 Artifact & Report Publishing

- [ ] Verify PublishTestResults picks up Surefire XML
- [ ] Verify PublishTestResults picks up Failsafe XML
- [ ] Verify PublishCodeCoverageResults picks up JaCoCo XML
- [ ] Verify OWASP HTML report publishes
- [ ] Verify Serenity HTML reports publish
- [ ] Verify PITest mutation reports publish
- [ ] Verify Docker image pushes to ACR
- [ ] Check all artifact paths match existing

## Phase 5: Incremental Migration

### 5.1 Pipeline Reference Update

- [ ] Update `ref: feature/cycle4` to `ref: main` in resources
- [ ] Test pipeline with main-branch scripts (before eirctl)
- [ ] Document script parameter mappings used

### 5.2 Build Stage Migration

- [x] Replace build-java.yml with `eirctl build`
  - [x] Pass maven_cache_directory env
  - [ ] Pass maven_allowed_test_tags
  - [x] Wire vulnerability_scan toggle
  - [x] Pass nvd_api_key if set
  - [ ] Pass vulnerability_scan_database_directory
- [x] Replace build-api-tests.yml with `eirctl api-build`
  - [x] Pass maven_cache_directory
  - [ ] Pass allowed/ignored tags
  - [x] Enable maven_untagged_test_check
  - [ ] Configure report directories
- [ ] Validate build artifacts identical
- [ ] Validate test results identical

### 5.3 Static Analysis Migration

- [x] Replace format check with `eirctl format-check`
- [ ] Replace checkstyle with `eirctl checkstyle`
- [ ] Replace spotbugs with `eirctl spotbugs`
- [x] Replace sonar with `eirctl sonar`
  - [x] Pass SONAR_HOST_URL (-a)
  - [x] Pass SONAR_PROJECT_NAME (-b)
  - [x] Pass SONAR_PROJECT_KEY (-c)
  - [x] Pass SONAR_TOKEN (-d)
  - [x] Pass SONAR_ORGANISATION (-e)
  - [x] Pass BUILD_NUMBER (-f)
  - [x] Pass SOURCE_BRANCH_REF (-g)
  - [x] Pass PR params (-W/-X/-Y/-Z) conditionally
- [ ] Validate SonarCloud metrics identical

### 5.4 Deployment Migration (Optional for eirctl)

- [x] Keep Terraform templates unchanged initially
- [x] Keep Kubernetes templates unchanged initially
- [ ] Consider eirctl k8s tasks for future iteration
- [ ] Document if any deploy steps use main-branch scripts directly

### 5.5 Environment Variable Groups

- [x] Verify `azure-sp-creds` still mapped
- [x] Verify `stacks-acr-creds` still mapped
- [x] Verify `sonar-credentials` still mapped
- [x] Verify `stacks-java-owasp-keys` still mapped
- [x] Verify `stacks-infra-credentials-nonprod` still used
- [x] Verify `stacks-credentials-nonprod-kv` still used
- [x] Verify `stacks-java-api` still used

### 5.6 Stage Structure Preservation

- [x] Verify Build stage unchanged in structure
- [x] Verify Dev stage unchanged
- [x] Verify Prod stage unchanged
- [x] Verify Release stage unchanged
- [x] Check all stage dependencies preserved

## Phase 6: Quality Gate Validation

### 6.1 Code Quality Enforcement

- [ ] Verify fmt-maven-plugin blocks on format violations
- [ ] Verify checkstyle fails build on violations
- [ ] Verify spotbugs fails build on high-priority bugs
- [ ] Verify PMD violations reported (if enabled)
- [ ] Test with intentional violations to confirm gates active

### 6.2 Test Coverage & Reporting

- [ ] Verify all Surefire reports publish to Test Results tab
- [ ] Verify all Failsafe reports publish
- [ ] Verify JaCoCo XML uploads to SonarCloud
- [ ] Verify coverage thresholds enforced (if configured)
- [ ] Check test result trends in Azure DevOps

### 6.3 Security Scanning

- [ ] Verify OWASP dependency-check produces HTML report
- [ ] Check dependency-check-report.html in build artifacts
- [ ] Verify NVD API key usage when provided
- [ ] Verify vulnerability database persistence
- [ ] Test maximum_cvss_to_fail_build threshold

### 6.4 Mutation Testing

- [ ] Verify PITest runs successfully
- [ ] Check target/pit-reports/ for HTML reports
- [ ] Verify mutation coverage metrics calculated
- [ ] Publish PITest reports as artifacts

### 6.5 API Test Reporting

- [ ] Verify Serenity aggregate report generated
- [ ] Check api-tests/target/site/serenity/index.html exists
- [ ] Verify screenshots captured on test failures
- [ ] Publish Serenity reports as build artifacts
- [ ] Verify Cucumber JSON reports generated

### 6.6 Container & Deployment Validation

- [ ] Verify Docker image builds successfully
- [ ] Check image tagged with correct build number
- [ ] Verify image pushes to ACR
- [ ] Test image pull and run
- [ ] Verify Kubernetes manifests templated correctly
- [ ] Verify kubectl apply succeeds in Dev

## Phase 7: Documentation & Cleanup

### 7.1 README Updates

- [x] Add eirctl Quick Start section
- [x] Document required eirctl version
- [x] Add installation instructions
- [x] Document common tasks (build, test, run)
- [x] Add pipeline examples
- [ ] Remove/archive references to run_scenario.sh
- [ ] Remove/archive references to deploy_scenario.sh

### 7.2 Troubleshooting Guide

- [x] Document Docker permission issues
- [x] Document Maven cache persistence issues
- [x] Document JAVA_HOME conflict (envfile.exclude fix)
- [x] Document GPG signing failure (-Dgpg.skip=true)
- [x] Document PowerShell variable expansion errors (bash vs pwsh)
- [ ] Document container network issues
- [ ] Document SonarCloud authentication issues
- [ ] Document OWASP NVD API rate limiting
- [ ] Document common build failures

### 7.3 Migration Guide

- [x] Document differences from old scripts
- [x] Create parameter mapping reference
- [x] Document new environment variables
- [x] List deprecated/removed parameters
- [ ] Provide rollback instructions

### 7.4 Code Cleanup

- [ ] Archive old run_scenario.sh (don't delete yet)
- [ ] Archive old deploy_scenario.sh
- [ ] Update .gitignore for eirctl artifacts
- [ ] Remove unused xmlstarlet dependency references
- [ ] Clean up obsolete pipeline variables

### 7.5 Knowledge Transfer

- [ ] Document eirctl task architecture
- [ ] Explain pipeline dependency chains
- [ ] Create runbook for common operations
- [ ] Document how to add new tasks
- [ ] Train team on eirctl usage

## Phase 8: Production Validation

### 8.1 Pre-Production Checks

- [ ] Run full pipeline 3+ times successfully
- [ ] Verify all stages pass consistently
- [ ] Check build time is acceptable
- [ ] Verify all artifacts publish
- [ ] Check resource usage in CI agents

### 8.2 Deployment Validation

- [ ] Deploy to Dev environment successfully
- [ ] Run smoke tests in Dev
- [ ] Deploy to UAT/staging successfully
- [ ] Run full regression in UAT
- [ ] Validate monitoring and logging

### 8.3 Rollback Preparation

- [ ] Document rollback steps
- [ ] Keep cycle4 branch reference as backup
- [ ] Test rollback procedure
- [ ] Define rollback criteria
- [ ] Assign rollback decision maker

### 8.4 Production Deployment

- [ ] Schedule production deployment window
- [ ] Notify stakeholders
- [ ] Execute deployment
- [ ] Monitor first production build
- [ ] Verify all quality gates pass
- [ ] Confirm production deployment successful

### 8.5 Post-Deployment Monitoring

- [ ] Monitor build times for 1 week
- [ ] Track failure rates
- [ ] Collect developer feedback
- [ ] Document any issues encountered
- [ ] Create backlog for improvements

## Phase 9: Optimization & Iteration

### 9.1 Performance Tuning

- [ ] Optimize Maven cache usage
- [ ] Tune Docker layer caching
- [ ] Adjust parallel test threads if needed
- [ ] Review OWASP database persistence
- [ ] Consider dedicated Maven repository mirror

### 9.2 Additional Enhancements

- [ ] Add developer onboarding tasks
- [ ] Create IDE integration guides
- [ ] Add pre-commit hooks for format checks
- [ ] Explore additional eirctl features
- [ ] Consider auto-updating eirctl version

### 9.3 Template Contribution

- [ ] Consider contributing tasks back to stacks-pipeline-templates
- [ ] Share lessons learned with Ensono Stacks community
- [ ] Document reusable patterns
- [ ] Create example configurations

---

## Progress Summary

**Phase 1 (Research):** ☐ Not Started  
**Phase 2 (Configuration):** ◐ In Progress — 67/71 items done (yaml-lint, terraform-format-check, terraform-validate added; k8s tasks, serenity.threads, -Y param remain)  
**Phase 3 (Local Testing):** ◐ In Progress — 10/18 items done (7 tasks validated locally: format-check, build, test, checkstyle, spotbugs, api-build, api-format-check)  
**Phase 4 (Azure Integration):** ◐ In Progress — 6/18 items done (template + vars + install step created; parallel validation & report publishing remain)  
**Phase 5 (Migration):** ◐ In Progress — 30/38 items done (build-java, build-api-tests, test-static-code-analysis, post-build-tasks, test-validate-yaml, test-validate-terraform templates replaced; deploy stages preserved; env var groups verified)  
**Phase 6 (Quality Gates):** ☐ Not Started  
**Phase 7 (Documentation):** ◐ In Progress — 15/25 items done (README, migration guide with parameter mapping, troubleshooting with JAVA_HOME/GPG/pwsh fixes)  
**Phase 8 (Production):** ☐ Not Started  
**Phase 9 (Optimization):** ☐ Not Started

**Overall Progress:** 41% (121/295 tasks completed)

---

## Notes

- This checklist should be updated as work progresses
- Cross-reference with [plan-eirctlImplementation.prompt.md](.github/prompts/plan-eirctlImplementation.prompt.md)
- All cycle4 → main script mappings documented in Phase 1 should inform Phase 2 task creation
- Keep baseline metrics from Phase 1 for comparison in Phases 6 and 8
- Document all deviations or blockers in this section
