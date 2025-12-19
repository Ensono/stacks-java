# Migrating to Eirctl

The `eirctl` command is a task automation tool for Ensono Stacks that provides consistent build, test, and deployment workflows across projects. This guide details how to integrate eirctl into the stacks-java project.

## Prerequisites

- Docker installed and running
- Azure DevOps pipeline access
- Latest eirctl version (check [releases](https://github.com/Ensono/eirctl/releases/latest))

## Migration Steps

### 1. Create eirctl Configuration Files

Create an `eirctl.yaml` file in the root of the project:

```yaml
import:
  - ./build/eirctl/contexts.yaml
  - ./build/eirctl/tasks.yaml
```

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
```

**Note:** Do not include `-NoProfile` in PowerShell contexts as eirctl relies on the profile to set up paths and import modules.

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
```

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
    value: 0.4.48  # Update to latest version
```

**Finding the latest version:**

- **PowerShell:**
  ```powershell
  ((Invoke-WebRequest https://api.github.com/repos/ensono/eirctl/releases/latest).content | ConvertFrom-Json).name
  ```

- **Bash:**
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

```
1.9.3
```

### 10. Local Testing

Test the eirctl setup locally:

```bash
# Install eirctl locally
wget https://github.com/Ensono/eirctl/releases/download/0.4.48/eirctl-linux-amd64 -O /tmp/eirctl
chmod +x /tmp/eirctl
sudo mv /tmp/eirctl /usr/local/bin/eirctl

# Verify installation
eirctl --version

# Run a task
eirctl build
```

## Benefits of Using Eirctl

- **Consistency**: Same commands work locally and in CI/CD
- **Containerized builds**: Isolated, reproducible build environments
- **Simplified pipelines**: Replace complex scripts with simple task names
- **Version control**: Task definitions are version-controlled with code
- **Cross-platform**: Works on Linux, macOS, and Windows

## Troubleshooting

### Docker not found
Ensure Docker is installed and the Docker daemon is running.

### Permission denied
Run eirctl with appropriate permissions or ensure your user is in the docker group.

### Container image pull failures
Verify network connectivity and Docker Hub access. For private registries, ensure authentication is configured.

## Next Steps

1. Review and customize tasks in `build/eirctl/tasks.yaml` for your specific needs
2. Update CI/CD pipelines to use eirctl commands
3. Document custom tasks for your team
4. Consider adding additional contexts for different build scenarios (e.g., AWS vs Azure)
