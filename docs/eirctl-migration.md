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
      shell: pwsh
      shell_args:
        - -Command
    envfile:
      exclude:
        - home
        - path
        - tmpdir
  
  # Optional: Local execution context (uses cross-platform mvdan shell)
  local:
    executable:
      bin: /bin/bash
      args:
        - -c
    quote: "'"
```

> [!NOTE]
>
> - Do not include `-NoProfile` in PowerShell contexts as eirctl relies on the profile to set up paths and import modules
> - eirctl uses the native Go API for OCI-compliant runtimes (docker, podman, containerd), so the Docker CLI is not required
> - Tasks without a context run in a [cross-platform shell](https://github.com/mvdan/sh) that works on Windows, Linux, and macOS
> - The `container` context offers better environment variable handling and supports task variations

### 4. Create Tasks Configuration

Create `build/eirctl/tasks.yaml` with common build tasks for the Java project:

```yaml
tasks:
  build:
    context: buildenv
    description: Build the Java application
    command:
      - ./mvnw clean package -DskipTests

  test:
    context: buildenv
    description: Run unit tests
    command:
      - ./mvnw test

  integration-test:
    context: buildenv
    description: Run integration tests
    command:
      - ./mvnw verify

  format:
    context: buildenv
    description: Format code using Google Java Style
    command:
      - ./mvnw fmt:format

  security-scan:
    context: buildenv
    description: Run OWASP dependency check
    command:
      - ./mvnw -P owasp-dependency-check verify
  
  # Conditional execution example
  build-if-changed:
    context: buildenv
    description: Build only if there are uncommitted changes
    command:
      - ./mvnw clean package
    condition: git diff --exit-code
  
  # Task with required variables
  deploy:
    context: buildenv
    description: Deploy application (requires ENVIRONMENT variable)
    command:
      - echo "Deploying to {{ .ENVIRONMENT }}"
      - ./mvnw deploy -Denv={{ .ENVIRONMENT }}
    required:
      env: [ENVIRONMENT]
      vars: []
```

**Available predefined variables for templates:**

- `.Root` - root config file directory
- `.Dir` - config file directory  
- `.TempDir` - system's temporary directory
- `.Args` - provided arguments as a string
- `.ArgsList` - array of provided arguments
- `.Task.Name` - current task's name
- `.Context.Name` - current task's execution context's name

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
    value: 0.9.7  # Latest stable version
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

Modify `build/azDevOps/azure/azure-pipelines-javaspring-k8s.yml` to install and use eirctl:

Add the installation step:

```yaml
steps:
  - template: templates/install-eirctl.yml
    parameters:
      EirctlVersion: $(EirctlVersion)
```

Replace direct Maven commands with eirctl tasks:

```yaml
# Before:
- script: ./mvnw clean package
  displayName: Build application

# After:
- script: eirctl build
  displayName: Build application
```

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

Ensure Docker is installed and the Docker daemon is running.

### Permission denied

Run eirctl with appropriate permissions or ensure your user is in the docker group.

### Container image pull failures

Verify network connectivity and Docker Hub access. For private registries, ensure authentication is configured.

## Advanced Configuration Examples

### Creating a Pipeline

For complex workflows, create a pipeline in `build/eirctl/tasks.yaml`:

```yaml
pipelines:
  ci:
    - task: format
      name: Check Code Format
      allow_failure: false
    
    - task: build
      name: Build Application
      depends_on: Check Code Format
    
    - task: test
      name: Unit Tests
      depends_on: Build Application
    
    - task: integration-test
      name: Integration Tests
      depends_on: Build Application
    
    - task: security-scan
      name: Security Scan
      depends_on: [Unit Tests, Integration Tests]
```

Run the pipeline: `eirctl run pipeline ci`

Visualize the execution graph: `eirctl graph ci`

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

## Next Steps

1. Review and customize tasks in `build/eirctl/tasks.yaml` for your specific needs
2. Update CI/CD pipelines to use eirctl commands
3. Create pipelines for complex workflows with dependencies
4. Document custom tasks for your team
5. Consider adding additional contexts for different build scenarios (e.g., AWS vs Azure)
6. Integrate the [JSON schema](https://raw.githubusercontent.com/Ensono/eirctl/refs/heads/main/schemas/schema_v1.json) into your IDE for validation
