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

- [ ] Create `build/eirctl/` directory
- [ ] Create `eirctl.yaml` root configuration
- [ ] Add JSON schema reference for IDE validation
- [ ] Configure imports to contexts.yaml and tasks.yaml

### 2.2 Contexts Configuration (`build/eirctl/contexts.yaml`)

- [ ] Define `buildenv` context with `ensono/eir-java:1.1.251`
- [ ] Define `infrastructure` context with `ensono/eir-infrastructure:1.1.251`
- [ ] Configure Maven cache host mount `-v ${PWD}/.m2:/root/.m2`
- [ ] Add M2_VOLUME override for named volume option
- [ ] Set `shell: pwsh` and `shell_args: [-Command]`
- [ ] Configure `envfile.exclude` for [home, path, tmpdir]
- [ ] Test context isolation and volume mounts

### 2.3 Task Library - Java Project (`build/eirctl/tasks.yaml`)

- [ ] Create `clean` task
- [ ] Create `cache-warm` task (dependency:go-offline)
- [ ] Create `build` task (install + compile)
- [ ] Create `build-skip-tests` task
- [ ] Create `test` task (-Dgroups="Unit,Component,Integration")
- [ ] Create `integration-test` task
- [ ] Create `verify` task
- [ ] Create `format` task
- [ ] Create `format-check` task
- [ ] Create `checkstyle` task
- [ ] Create `spotbugs` task
- [ ] Create `pitest` task
- [ ] Create `security-scan` task (OWASP with -P owasp-dependency-check)
- [ ] Create `sonar` task with dynamic properties
- [ ] Create `dependency-tree` task
- [ ] Add M2_LOCATION env to all Maven tasks

### 2.4 Task Library - API Tests (`build/eirctl/tasks.yaml`)

- [ ] Create `api-clean` task (dir: ../api-tests)
- [ ] Create `api-build` task
- [ ] Create `api-test-functional` task (Functional | Smoke tags)
- [ ] Create `api-test-smoke` task
- [ ] Create `api-test-performance` task
- [ ] Create `api-test-untagged-check` task
- [ ] Add THREADS variable (default: 4) to test tasks
- [ ] Configure -Dthreads=${THREADS} propagation
- [ ] Configure -Dserenity.threads=${THREADS} where applicable
- [ ] Add ignore tags support (-Y parameter)

### 2.5 Task Library - Cloud Scenarios

- [ ] Create `run-azure` task (-Pazure,-aws)
- [ ] Create `run-aws` task (-Paws,-azure)
- [ ] Create `run-all-clouds` task (-Pazure,aws)
- [ ] Create `package-azure` task (skip tests, azure profile)
- [ ] Create `package-aws` task (skip tests, aws profile)

### 2.6 Task Library - Deployment & Infrastructure

- [ ] Create `docker-build` task
- [ ] Create `k8s-envsubst` task (deploy-k8s-envsubst.bash wrapper)
- [ ] Create `k8s-apply` task (deploy-k8s-apply.bash wrapper)
- [ ] Create `k8s-rollout-status` task
- [ ] Create `terraform-validate` task
- [ ] Create `terraform-plan` task
- [ ] Create `terraform-apply` task

### 2.7 Pipeline Definitions

- [ ] Define `ci-build` pipeline
  - [ ] format-check stage (allow_failure: false)
  - [ ] Parallel build-azure and build-aws
  - [ ] test stage (depends on builds)
  - [ ] integration-test stage
  - [ ] Parallel security-scan and sonar (depends on tests)
  - [ ] api-build (depends on java verify)
  - [ ] api-test-functional (depends on api-build)
- [ ] Define `quality-gate` pipeline
  - [ ] Parallel: format-check, checkstyle, spotbugs, pitest, security-scan
- [ ] Define `local-dev` pipeline
  - [ ] build-skip-tests → cache-warm → run-azure

### 2.8 Configuration Validation

- [ ] Run `eirctl validate` to check schema
- [ ] Verify all task references resolve
- [ ] Check pipeline dependency chains
- [ ] Validate context mounts and permissions

## Phase 3: Local Testing

### 3.1 Eirctl Installation

- [ ] Download eirctl 0.9.7 for Linux AMD64
- [ ] Install to `/usr/local/bin/eirctl`
- [ ] Verify installation: `eirctl --version`
- [ ] Check eirctl validate passes

### 3.2 Individual Task Testing

- [ ] Test `eirctl cache-warm` - verify .m2 populates
- [ ] Test `eirctl build` - compare with direct mvnw
- [ ] Test `eirctl test` - verify test reports
- [ ] Test `eirctl format-check` - verify fmt enforcement
- [ ] Test `eirctl checkstyle`
- [ ] Test `eirctl spotbugs`
- [ ] Test `eirctl security-scan` - verify OWASP report
- [ ] Test `eirctl sonar` (with mock/local SonarQube)
- [ ] Test `eirctl api-build`
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

- [ ] Create `build/azDevOps/azure/templates/install-eirctl.yml`
- [ ] Add Bash@3 task for downloading eirctl 0.9.7
- [ ] Set executable permissions
- [ ] Move to /usr/local/bin or agent tools directory
- [ ] Test template in isolation

### 4.2 Pipeline Variables

- [ ] Add `EirctlVersion: 0.9.7` to azuredevops-vars.yml
- [ ] Document all required env vars for eirctl tasks
- [ ] Map pipeline vars to eirctl task env
- [ ] Add THREADS parameter (default: 4)
- [ ] Preserve M2 cache configuration

### 4.3 Parallel Validation Run

- [ ] Add install-eirctl step to azure-pipelines-javaspring-k8s.yml
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

- [ ] Replace build-java.yml with `eirctl build`
  - [ ] Pass maven_cache_directory env
  - [ ] Pass maven_allowed_test_tags
  - [ ] Wire vulnerability_scan toggle
  - [ ] Pass nvd_api_key if set
  - [ ] Pass vulnerability_scan_database_directory
- [ ] Replace build-api-tests.yml with `eirctl api-build`
  - [ ] Pass maven_cache_directory
  - [ ] Pass allowed/ignored tags
  - [ ] Enable maven_untagged_test_check
  - [ ] Configure report directories
- [ ] Validate build artifacts identical
- [ ] Validate test results identical

### 5.3 Static Analysis Migration

- [ ] Replace format check with `eirctl format-check`
- [ ] Replace checkstyle with `eirctl checkstyle`
- [ ] Replace spotbugs with `eirctl spotbugs`
- [ ] Replace sonar with `eirctl sonar`
  - [ ] Pass SONAR_HOST_URL (-a)
  - [ ] Pass SONAR_PROJECT_NAME (-b)
  - [ ] Pass SONAR_PROJECT_KEY (-c)
  - [ ] Pass SONAR_TOKEN (-d)
  - [ ] Pass SONAR_ORGANISATION (-e)
  - [ ] Pass BUILD_NUMBER (-f)
  - [ ] Pass SOURCE_BRANCH_REF (-g)
  - [ ] Pass PR params (-W/-X/-Y/-Z) conditionally
- [ ] Validate SonarCloud metrics identical

### 5.4 Deployment Migration (Optional for eirctl)

- [ ] Keep Terraform templates unchanged initially
- [ ] Keep Kubernetes templates unchanged initially
- [ ] Consider eirctl k8s tasks for future iteration
- [ ] Document if any deploy steps use main-branch scripts directly

### 5.5 Environment Variable Groups

- [ ] Verify `azure-sp-creds` still mapped
- [ ] Verify `stacks-acr-creds` still mapped
- [ ] Verify `sonar-credentials` still mapped
- [ ] Verify `stacks-java-owasp-keys` still mapped
- [ ] Verify `stacks-infra-credentials-nonprod` still used
- [ ] Verify `stacks-credentials-nonprod-kv` still used
- [ ] Verify `stacks-java-api` still used

### 5.6 Stage Structure Preservation

- [ ] Verify Build stage unchanged in structure
- [ ] Verify Dev stage unchanged
- [ ] Verify Prod stage unchanged
- [ ] Verify Release stage unchanged
- [ ] Check all stage dependencies preserved

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

- [ ] Add eirctl Quick Start section
- [ ] Document required eirctl version
- [ ] Add installation instructions
- [ ] Document common tasks (build, test, run)
- [ ] Add pipeline examples
- [ ] Remove/archive references to run_scenario.sh
- [ ] Remove/archive references to deploy_scenario.sh

### 7.2 Troubleshooting Guide

- [ ] Document Docker permission issues
- [ ] Document Maven cache persistence issues
- [ ] Document container network issues
- [ ] Document SonarCloud authentication issues
- [ ] Document OWASP NVD API rate limiting
- [ ] Document common build failures

### 7.3 Migration Guide

- [ ] Document differences from old scripts
- [ ] Create parameter mapping reference
- [ ] Document new environment variables
- [ ] List deprecated/removed parameters
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

**Phase 1 (Research):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 2 (Configuration):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 3 (Local Testing):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 4 (Azure Integration):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 5 (Migration):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 6 (Quality Gates):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 7 (Documentation):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 8 (Production):** ☐ Not Started / ◐ In Progress / ☑ Complete  
**Phase 9 (Optimization):** ☐ Not Started / ◐ In Progress / ☑ Complete

**Overall Progress:** 0% (0/209 tasks completed)

---

## Notes

- This checklist should be updated as work progresses
- Cross-reference with [plan-eirctlImplementation.prompt.md](.github/prompts/plan-eirctlImplementation.prompt.md)
- All cycle4 → main script mappings documented in Phase 1 should inform Phase 2 task creation
- Keep baseline metrics from Phase 1 for comparison in Phases 6 and 8
- Document all deviations or blockers in this section
