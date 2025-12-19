# Copilot Instructions for Stacks Java

> **Security Note**: All contributors must follow the security and compliance guidelines defined in [copilot-security-instructions.md](./copilot-security-instructions.md).

## Project Overview

This is a **Spring Boot REST API starter application** from Ensono Stacks, demonstrating a menu management system. It's part of the Stacks family with three variants:
1. **stacks-java**: Simple REST API (no persistence)
2. **stacks-java-cqrs**: REST API with CQRS pattern + CosmosDB
3. **stacks-java-cqrs-events**: Full CQRS + Events + ServiceBus integration

This project inherits from `stacks-modules-parent` POM for dependency management.

## Key Architecture Patterns

### Project Structure Convention

All workload projects follow this structure:
```
<project-root>/
├── java/                    # Main Spring Boot application
│   ├── pom.xml             # Inherits from stacks-modules-parent
│   └── src/main/java/com/amido/stacks/workloads/
├── api-tests/              # Pact contract tests (JUnit)
├── api-tests-karate/       # Karate BDD tests (events variant only)
├── build/azDevOps/azure/   # Azure DevOps pipeline definitions
└── stackscli.yml           # Stacks CLI configuration for scaffolding
```

### Domain-Driven Package Organization

Code is organized by domain feature, not technical layer:
```
com.amido.stacks.workloads.menu/
├── api/v1/                 # REST controllers (versioned)
├── commands/               # CQRS commands (create/update/delete)
├── handlers/               # Command handlers (business logic)
├── domain/                 # Domain entities (Menu, Category, Item)
├── service/                # Domain services
├── repository/             # Persistence interfaces
├── events/                 # Domain events (events variant only)
└── mappers/                # DTO ↔ Domain mapping
```

**Pattern**: Avoid traditional "controller/service/repository" packages. Group by feature/aggregate root.

### CQRS Implementation (stacks-java-cqrs variants)

Commands flow through dedicated handlers:
```java
// 1. Controller receives request → maps to command
@PostMapping
ResponseEntity<ResourceCreatedResponse> createMenu(@RequestBody CreateMenuRequest request) {
    CreateMenuCommand command = mapper.map(request);
    UUID id = createMenuHandler.handle(command);
    return created(id);
}

// 2. Handler processes command → updates domain → persists
@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {
    @Override
    public Optional<UUID> handle(CreateMenuCommand command) {
        Menu menu = new Menu(/* ... */);
        menuService.create(menu);
        return Optional.of(menu.getId());
    }
}
```

**Key Classes**:
- `MenuBaseCommandHandler<T>`: Base class for handlers that load existing menu first
- `CommandHandler<T>`: Interface for all command handlers
- Commands extend `MenuCommand` with correlation IDs for tracing

### Event-Driven Architecture (stacks-java-cqrs-events)

Handlers publish domain events after successful operations:
```java
@Component
public class UpdateMenuHandler extends MenuBaseCommandHandler<UpdateMenuCommand> {
    @Override
    Optional<UUID> handleCommand(Menu menu, UpdateMenuCommand command) {
        menuService.update(menu, command);
        publishEvents(raiseApplicationEvents(menu, command));  // ← Publishes to ServiceBus
        return Optional.of(command.getMenuId());
    }
    
    @Override
    List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateMenuCommand command) {
        return Collections.singletonList(new MenuUpdatedEvent(command));
    }
}
```

**Event Pattern**: Each command raises specific events with numeric codes (e.g., 101=MenuCreated, 102=MenuUpdated).

## Essential Build Commands

```bash
# Run application locally (from java/ directory)
./mvnw spring-boot:run

# Run with CosmosDB Emulator (CQRS variants)
./mvnw spring-boot:run \
  -Dspring-boot.run.jvmArguments='-Djavax.net.ssl.trustStore="<cosmos-cert-path>" -Djavax.net.ssl.trustStorePassword="changeit"'

# Build Docker image
docker build --tag stacks:1.0 .

# Run tests
./mvnw test                              # Unit tests
./mvnw verify                            # Unit + integration tests
./mvnw -P owasp-dependency-check verify  # Security scan

# Format code (Google Java Style - runs automatically on build)
./mvnw fmt:format
```

## Environment Configuration

### Required Environment Variables (CQRS variants)

- `AZURE_COSMOSDB_KEY`: CosmosDB connection key
- `AZURE_APPLICATION_INSIGHTS_INSTRUMENTATION_KEY`: AppInsights logging

**Disable AppInsights** for local dev by setting `application-insights.enabled: false` in `application.yml`.

### Auth0 Integration (Optional)

Set `auth.isEnabled=true` in `auth.properties` to enable JWT validation. Requires:
- `auth0.issuer`: Auth0 tenant URL
- `auth0.apiAudience`: API identifier

## Module Dependencies

This project uses Stacks modules for common functionality:

| Module | Purpose | Usage Pattern |
|--------|---------|---------------|
| `stacks-core-api` | REST controllers, exception handling, correlation IDs | Auto-imported |
| `stacks-core-commons` | Shared interfaces (`StacksPersistence`) | Auto-imported |
| `stacks-core-cqrs` | Command handler interfaces | CQRS variants only |
| `stacks-core-messaging` | Event publisher/listener contracts | Events variant only |
| `stacks-azure-cosmos` | CosmosDB repository implementation | Azure CQRS variants |
| `stacks-aws-dynamodb` | DynamoDB repository implementation | AWS variants |
| `stacks-azure-servicebus` | ServiceBus event publisher | Events variant only |

**Pattern**: Extend `StacksCosmosRepository<T>` or `StacksDynamoDbRepository<T>` for persistence - CRUD methods auto-implemented.

## Testing Strategy

### Unit Tests
- **Location**: `src/test/java` alongside production code
- **Tools**: JUnit 5 + Mockito + AssertJ
- **Pattern**: Test handlers and services in isolation, mock repositories

### Integration Tests
- **Location**: Same test directory, use `*IT.java` suffix
- **Pattern**: Use `@SpringBootTest` to test full application context

### Contract Tests (Pact)
- **Location**: `api-tests/` directory (separate project)
- **Run**: `mvn test` in api-tests directory
- **Pattern**: Consumer-driven contracts verified against running app

### API Tests (Karate)
- **Location**: `api-tests-karate/` (events variant only)
- **Pattern**: BDD-style feature files for E2E scenarios

## CI/CD Pipeline

### Azure DevOps Integration
- **Pipeline**: `build/azDevOps/azure/azure-pipelines-javaspring-k8s.yml`
- **Templates**: From `Ensono/stacks-pipeline-templates` repo
- **Container Images**: Uses `ensono/eir-java:1.1.251` for builds

### Versioning Pattern
```
<major>.<minor>.<patch>-<branch>-<revision>
Example: 1.0.1-master-42
```

### Build Stages
1. **Build**: Maven compile, fmt check, tests
2. **Security**: OWASP dependency scan
3. **Containerize**: Docker image build
4. **Deploy**: Kubernetes deployment (if configured)
5. **API Tests**: Pact + Karate verification

## Scaffolding and Code Generation

This project supports customization via **Stacks CLI** (`stackscli.yml`):

```bash
# Generate custom archetype with your namespace
mvn clean archetype:create-from-project \
  -f ./java/pom.xml \
  -DpropertyFile=./java/archetype.properties
```

**Pattern**: Use `stackscli.yml` replacements to rebrand company/project names during scaffolding.

## Common Issues & Solutions

### CosmosDB SSL Certificate (Local Dev)
When using CosmosDB Emulator, export its certificate and pass via JVM args:
```bash
-Djavax.net.ssl.trustStore="/path/to/cosmos-cert.jks"
```

### Code Formatting Failures
If build fails on format check: `./mvnw fmt:format` before committing. Auto-formatting is enforced.
Always run the formatter over every module that has modified files (e.g., `./mvnw fmt:format` in `java/` and `api-tests/`) before pushing or opening a PR to avoid fmt check failures.

### Missing Parent POM
If Maven can't resolve `stacks-modules-parent`, install it locally:
```bash
cd ../stacks-java-module-parent
./mvnw clean install
```

### Lombok + MapStruct Conflicts
Parent POM configures annotation processor ordering - do not override `annotationProcessorPaths`.

## Key Files to Reference

- `java/pom.xml`: Dependency versions, profiles, plugin configuration
- `java/src/main/resources/application.yml`: Spring Boot configuration
- `java/src/main/resources/auth.properties`: Auth0 settings
- `build/azDevOps/azure/azuredevops-vars.yml`: Pipeline variables
- `stackscli.yml`: Scaffolding configuration

## API Documentation

- **Swagger UI**: http://localhost:9000/swagger/index.html (when running)
- **OpenAPI JSON**: http://localhost:9000/swagger/oas.json
- **Health Check**: http://localhost:9000/health
- **Sample Endpoint**: http://localhost:9000/v1/menu
