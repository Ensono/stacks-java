# Migrating to Eirctl

The `eirctl` command is a concurrent task and container runner that provides consistent build, test, and deployment workflows. It's cross-platform (Windows, Linux, macOS) and cross-architecture (amd64, arm64), making it a powerful alternative to GNU Make and other build tools.

Whilst built within the Ensono ecosystem, eirctl can be used independently of Ensono Stacks. This guide details how to integrate eirctl into the stacks-java project.

## Prerequisites

- Docker installed and running (for containerized contexts)
- Azure DevOps pipeline access
- Latest eirctl version: **0.9.7** ([releases](https://github.com/Ensono/eirctl/releases/latest))

## Migration Steps

### 1. Create eirctl Configuration Files

Create an `eirctl.yaml` file in the root of the project:

```yaml
import:
  - ./build/eirctl/contexts.yaml
  - ./build/eirctl/tasks.yaml
```

This configuration follows eirctl's modular design. The [schema](https://raw.githubusercontent.com/Ensono/eirctl/refs/heads/main/schemas/schema_v1.json) can be integrated into your IDE for validation and autocomplete.

### 2. Create eirctl Directory Structure

Create the `build/eirctl` directory:

```bash
mkdir -p build/eirctl
```

### 3. Create Contexts Configuration

Create `build/eirctl/contexts.yaml` with the Java build environment context:

```yaml
contexts:
  buildenv:
    container:
      name: ensono/eir-java:1.1.251
      shell: bash
      shell_args:
        - -c
      volumes:
        - "${M2_CACHE_DIR:-./.m2}:/root/.m2"
    envfile:
      exclude:
        - home
        - path
        - tmpdir
        - java_home
        - java_opts
        - maven_home
        - m2_home

  infrastructure:
    container:
      name: ensono/eir-infrastructure:1.1.251
      shell: bash
      shell_args:
        - -c
    envfile:
      exclude:
        - home
        - path
        - tmpdir
        - java_home
```

> [!IMPORTANT]
>
> - **Use `bash` not `pwsh`** — all commands use bash syntax for variable expansion (`${VAR:-default}`), and `mvnw` is a shell script. Both container images include bash at `/usr/bin/bash`.
> - **Exclude `java_home`** — the host's JAVA_HOME (e.g., from sdkman) would override the container's `/usr/local/java`, causing build failures.
> - **Maven cache mount** — the `volumes` entry bind-mounts `.m2/` from the project root into the container for dependency caching across runs. Ensure the `.m2` directory exists (`mkdir -p .m2`).
> - eirctl uses the native Go API for OCI-compliant runtimes (docker, podman, containerd), so the Docker CLI is not required.
> - Tasks without a context run in a [cross-platform shell](https://github.com/mvdan/sh) that works on Windows, Linux, and macOS.

### 4. Create Tasks Configuration

Create `build/eirctl/tasks.yaml` with tasks for the Java project. Key design decisions:

- **`build` uses `install` goal** (not `package`) so artifacts are available in the local repo for `api-tests` consumption
- **`-Dgpg.skip=true`** bypasses GPG signing (inherited from parent POM) in containerized builds where no key is available
- **`-Dgroups="Unit | Component | Integration"`** uses pipe-delimited JUnit 5 tags matching the pipeline convention
- **Property overrides replace `-P` flags** for cloud profiles because Maven `-P` cannot reliably deactivate file-based profile activation
- **`sonar-scanner` CLI** is used for SonarCloud (not the Maven plugin), reading `sonar-project.properties`

```yaml
tasks:
  build:
    context: buildenv
    description: Build the Java application and install to local repo
    dir: java
    command:
      - >-
        ./mvnw clean install
        -Dgpg.skip=true
        -Dmaven.repo.local=${M2_CACHE_DIR:-./.m2}
        --no-transfer-progress

  test:
    context: buildenv
    description: Run unit, component, and integration tests
    dir: java
    command:
      - >-
        ./mvnw test
        -Dgroups="Unit | Component | Integration"
        -Dmaven.repo.local=${M2_CACHE_DIR:-./.m2}
        --no-transfer-progress

  format-check:
    context: buildenv
    description: Check code formatting without modifying files
    dir: java
    command:
      - >-
        ./mvnw fmt:check
        -Dmaven.repo.local=${M2_CACHE_DIR:-./.m2}
        --no-transfer-progress

  run-azure:
    context: buildenv
    description: Run with Azure profile (property overrides, not -P flags)
    dir: java
    command:
      - >-
        ./mvnw spring-boot:run
        -Dazure.profile.name=azure
        -Daws.profile.name=no-aws
        -Dmaven.repo.local=${M2_CACHE_DIR:-./.m2}

  sonar:
    context: buildenv
    description: Run SonarCloud branch analysis using sonar-scanner CLI
    dir: java
    command:
      - >-
        sonar-scanner
        -Dsonar.host.url=${SONAR_HOST_URL:-https://sonarcloud.io}
        -Dsonar.projectKey=${SONAR_PROJECT_KEY}
        -Dsonar.token=${SONAR_TOKEN}
        -Dsonar.organization=${SONAR_ORGANISATION}
        -Dsonar.branch.name=${SOURCE_BRANCH_REF}
    required:
      env:
        - SONAR_PROJECT_KEY
        - SONAR_TOKEN
        - SONAR_ORGANISATION
        - SOURCE_BRANCH_REF
```

See `build/eirctl/tasks.yaml` for the complete task library (34 tasks).

### 5. Create Azure DevOps Installation Template

Create `build/azDevOps/azure/templates/install-eirctl.yml`:

```yaml
parameters:
  - name: EirctlVersion
    type: string

steps:
  # Install Eirctl so that the build tasks can be run
  - task: Bash@3
    displayName: "Install: Eirctl"
    inputs:
      targetType: inline
      script: |
        sudo wget https://github.com/Ensono/eirctl/releases/download/${{ parameters.EirctlVersion }}/eirctl-linux-amd64 -O /usr/local/bin/eirctl
        sudo chmod +x /usr/local/bin/eirctl
```

### 6. Update Azure DevOps Variables

Update `build/azDevOps/azure/azuredevops-vars.yml` to include the eirctl version:

```yaml
variables:
  - name: EirctlVersion
    value: 0.9.11  # Latest stable version
```

#### Finding the latest version

##### PowerShell

  ```powershell
  ((Invoke-WebRequest https://api.github.com/repos/ensono/eirctl/releases/latest).content | ConvertFrom-Json).name
  ```

##### Bash

  ```bash
  curl -s https://api.github.com/repos/ensono/eirctl/releases/latest | jq -r .name
  ```

### 7. Update Main Pipeline

Modify `build/azDevOps/azure/azure-pipelines-javaspring-k8s.yml` to install and use eirctl.

#### Install eirctl

Add the installation step early in each job:

```yaml
steps:
  - template: templates/install-eirctl.yml
    parameters:
      EirctlVersion: $(EirctlVersion)
```

#### Replace cycle4 template calls with eirctl tasks

The key migration pattern is: **replace each `stacks-pipeline-templates` template** with (1) an `eirctl` task call plus (2) explicit Azure DevOps publish steps.

The cycle4 templates bundled build + publish into a single template. With eirctl, builds are separated from publishing because eirctl runs inside a container while Azure DevOps publish tasks must run on the agent.

```yaml
# BEFORE (cycle4 template):
- template: templates/steps/build/build-java.yml@templates
  parameters:
    repo_root_dir: $(Build.Repository.LocalPath)
    project_root_dir: $(Build.Repository.LocalPath)/java
    # ... many parameters

# AFTER (eirctl + explicit publish):
- script: eirctl build
  displayName: "Eirctl: Build Java Application"

- task: PublishTestResults@2
  displayName: "Publish: Surefire Test Results"
  inputs:
    testResultsFormat: JUnit
    testResultsFiles: "**/surefire-reports/TEST-*.xml"
    searchFolder: $(Build.Repository.LocalPath)/java
    testRunTitle: "Unit Tests"
  condition: always()

- task: PublishCodeCoverageResults@1
  displayName: "Publish: JaCoCo Coverage"
  inputs:
    codeCoverageTool: JaCoCo
    summaryFileLocation: "**/jacoco/test/jacocoTestReport.xml"
    reportDirectory: "$(Build.Repository.LocalPath)/java/target/site/jacoco"
  condition: succeededOrFailed()

- task: PublishPipelineArtifact@1
  displayName: "Publish: Build Artifact"
  inputs:
    targetPath: $(Build.Repository.LocalPath)/java/target
    artifact: java-build
```

#### Templates replaced so far

| Cycle4 Template                         | Eirctl Replacement                                                                                          | Status  |
|-----------------------------------------|-------------------------------------------------------------------------------------------------------------|---------|
| `build-java.yml`                        | `eirctl build` + PublishTestResults + PublishCodeCoverage + PublishPipelineArtifact                         | Done    |
| `build-api-tests.yml`                   | `eirctl api-build` + `eirctl api-format-check` + `eirctl api-test-untagged-check` + PublishPipelineArtifact | Done    |
| `test-static-code-analysis.yml`         | `eirctl sonar` (branch) / `eirctl sonar-pr` (PR)                                                            | Done    |
| `post-build-tasks.yml`                  | `eirctl security-scan` + `eirctl api-security-scan` + PublishPipelineArtifact                               | Done    |
| `test-validate-yaml.yml`                | `eirctl yaml-lint`                                                                                          | Done    |
| `test-validate-terraform.yml`           | `eirctl terraform-format-check` + `eirctl terraform-validate`                                               | Done    |
| `build-docker-image.yml`                | Not yet migrated                                                                                            | Pending |
| `deploy-infra.yml`                      | Not yet migrated (deploy stage)                                                                             | Pending |
| `deploy-app.yml`                        | Not yet migrated (deploy stage)                                                                             | Pending |
| `deploy-post-deploy-tests-cucumber.yml` | Not yet migrated (deploy stage)                                                                             | Pending |

> [!NOTE]
> The pipeline `ref` remains `feature/cycle4` until all remaining templates are replaced. Do **not** switch to `ref: main` while any cycle4-only templates are still in use.

### 8. Optional: GitHub Actions Support

If using GitHub Actions, create `.github/actions/install-eirctl/action.yml`:

```yaml
name: 'Install eirctl'
description: 'Downloads and installs eirctl'
inputs:
  version:
    description: 'Eirctl version to install'
    required: true
runs:
  using: 'composite'
  steps:
    - run: |
        rm -rf /usr/local/bin/eirctl
        wget https://github.com/Ensono/eirctl/releases/download/${{ inputs.version }}/eirctl-linux-amd64 -O /usr/local/bin/eirctl
        chmod +x /usr/local/bin/eirctl
      shell: bash
```

### 9. Add Terraform Version (if applicable)

If your project uses Terraform, create a `.terraform-version` file in the root:

```text
1.9.3
```

### 10. Local Testing

Test the eirctl setup locally:

```bash
# Install eirctl locally
wget https://github.com/Ensono/eirctl/releases/download/0.9.7/eirctl-linux-amd64 -O /tmp/eirctl
chmod +x /tmp/eirctl
sudo mv /tmp/eirctl /usr/local/bin/eirctl

# Verify installation
eirctl --version

# Initialize a sample configuration (optional)
eirctl init

# Validate your configuration
eirctl validate

# List available tasks
eirctl list tasks

# Run a task (both syntaxes are equivalent)
eirctl run task build
eirctl build

# Run a task with arguments
eirctl test -- --fail-fast

# Run a task with custom variables
eirctl deploy --set ENVIRONMENT=staging
```

## Key Features

### Core Benefits

- **Consistency**: Same commands work locally and in CI/CD
- **Containerized builds**: Isolated, reproducible build environments using native OCI APIs
- **Simplified pipelines**: Replace complex scripts with simple task names
- **Version control**: Task definitions are version-controlled with code
- **Cross-platform**: Works on Linux, macOS, and Windows (amd64 and arm64)

### Advanced Features

- **Pipelines**: Chain tasks with dependency management and parallel execution
- **Task Variations**: Run the same task multiple times with different environment variables
- **Conditional Execution**: Skip tasks based on conditions (e.g., git status)
- **Output Formats**: Choose from `raw`, `prefixed`, or `cockpit` (dashboard) output
- **Execution Graphs**: Visualize pipeline execution with `eirctl graph`
- **Shell Access**: Jump into a container context with `eirctl shell <context>`
- **Watchers**: Monitor file changes and trigger tasks automatically
- **Artifacts**: Store and retrieve task outputs for use in subsequent tasks

## Troubleshooting

### Docker not found

Ensure Docker is installed and the Docker daemon is running. eirctl uses the native Go OCI API, not the Docker CLI.

### Permission denied

Ensure your user is in the docker group: `sudo usermod -aG docker $USER` then log out and back in.

### Container image pull failures

Verify network connectivity and Docker Hub access. For private registries, ensure authentication is configured.

### JAVA_HOME conflict ("javac: invalid target release")

The host's `JAVA_HOME` leaks into the container via eirctl's envfile mechanism, overriding the container's JDK. Fix by adding `java_home` to `envfile.exclude` in your context:

```yaml
envfile:
  exclude:
    - java_home
    - java_opts
    - maven_home
    - m2_home
```

### GPG signing failure ("no default secret key")

The parent POM includes `maven-gpg-plugin` bound to the `install` lifecycle. In containerized builds without a GPG key, add `-Dgpg.skip=true` to your Maven commands.

### .m2 directory missing ("statfs .m2: no such file")

The volume mount `${M2_CACHE_DIR:-./.m2}:/root/.m2` requires the host directory to exist. Run `mkdir -p .m2` before the first build.

### PowerShell variable expansion errors

Bash-style `${VAR:-default}` syntax does not work in PowerShell. Use `shell: bash` and `shell_args: [-c]` for contexts that run bash commands or Maven wrapper scripts.

## Advanced Configuration Examples

### Using Pipelines

The project includes three predefined pipelines in `build/eirctl/tasks.yaml`:

```yaml
pipelines:
  ci-build:
    - task: clean
      name: Clean Build Artifacts
    - task: format-check
      name: Check Code Format
      depends_on: Clean Build Artifacts
    - task: build
      name: Build Application
      depends_on: Check Code Format
    - task: api-build
      name: Build API Tests
      depends_on: Build Application
    - task: api-format-check
      name: Check API Test Format
      depends_on: Build API Tests
    - task: checkstyle
      name: Run Checkstyle
      depends_on: Build Application
    - task: spotbugs
      name: Run SpotBugs
      depends_on: Build Application
    - task: pitest
      name: Mutation Testing
      depends_on: Build Application
    - task: dependency-tree
      name: Dependency Tree
      depends_on: Build Application

  quality-gate:
    - task: format-check
      name: Format Check
    - task: checkstyle
      name: Checkstyle
    - task: spotbugs
      name: SpotBugs
    - task: test
      name: Tests
    - task: security-scan
      name: Security Scan

  local-dev:
    - task: format
      name: Format Code
    - task: build
      name: Build
      depends_on: Format Code
    - task: test
      name: Test
      depends_on: Build
```

Run a pipeline: `eirctl run pipeline ci-build`

Visualize the execution graph: `eirctl graph ci-build`

### Task Variations

Build for multiple platforms:

```yaml
tasks:
  docker-build:
    description: Build Docker images for multiple platforms
    command:
      - docker build --platform ${PLATFORM} -t myapp:${TAG} .
    variations:
      - PLATFORM: linux/amd64
        TAG: amd64
      - PLATFORM: linux/arm64
        TAG: arm64
```

### Available CLI Commands

- `eirctl init` - Initialize with a sample config file
- `eirctl validate` - Validate your configuration
- `eirctl list [contexts|pipelines|tasks|watchers]` - List available resources
- `eirctl run [pipeline|task] <name>` - Run a pipeline or task
- `eirctl show <task>` - Show task details
- `eirctl graph <pipeline>` - Visualize pipeline execution graph
- `eirctl shell <context>` - Shell into a container context (beta)
- `eirctl watch` - Watch for file changes and run tasks
- `eirctl completion [bash|fish|powershell|zsh]` - Generate shell completion

> [!TIP]
> `eirctl <task>` is shorthand for `eirctl run task <task>`

## Current Migration Status

The build stage of the Azure DevOps pipeline has been fully migrated to eirctl. The deploy stages (Dev, Prod) still use `stacks-pipeline-templates` on `ref: feature/cycle4`.

**Completed:**
- All build-stage templates replaced with eirctl task calls
- Validation templates (YAML lint, Terraform) replaced
- SonarCloud analysis (branch + PR) migrated
- Local task testing validated (format-check, build, test, checkstyle, spotbugs, api-build, api-format-check)

**Remaining:**
- Docker image build template migration
- Deploy-stage template migration (infrastructure, application, post-deploy tests)
- Pipeline `ref` switch from `feature/cycle4` to `main`
- CI pipeline validation in Azure DevOps

## Next Steps

1. Replace `build-docker-image.yml` template with eirctl `docker-build` task
2. Migrate deploy stages to eirctl tasks (or keep as templates if preferred)
3. Switch pipeline `ref` to `main` once all cycle4 templates are replaced
4. Run full CI pipeline in Azure DevOps to validate
5. Integrate the [JSON schema](https://raw.githubusercontent.com/Ensono/eirctl/refs/heads/main/schemas/schema_v1.json) into your IDE for validation and autocomplete
