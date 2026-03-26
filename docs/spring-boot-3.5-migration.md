# Spring Boot 3.5.x Migration - Parent POM Requirements

## Overview

The `stacks-modules-parent:3.0.98` brings in Spring Boot 3.5.7, which introduces several breaking changes and compatibility issues that need to be addressed either in the parent POM or in downstream projects.

## Issues Identified

### 1. Spring Cloud Version Incompatibility

**Problem:**  
The current Spring Cloud version (`2022.0.4`) is incompatible with Spring Boot 3.5.7.

```text
Spring Boot [3.5.7] is not compatible with this Spring Cloud release train.
Change Spring Boot version to one of the following versions [3.0.x, 3.1.x].
```

**Required Fix in Parent POM:**  
Update `spring.cloud.dependencies.version` to a release train that Spring Boot 3.5.x accepts without disabling the verifier:

- Spring Boot 3.0.x / 3.1.x: Spring Cloud 2022.0.x (Kilburn)
- Spring Boot 3.2.x: Spring Cloud 2023.0.x (Leyton)
- Spring Boot 3.3.x / 3.4.x: Spring Cloud 2024.0.x
- Spring Boot 3.5.x: this repository is temporarily pinned to Spring Cloud 2024.0.3 and disables the compatibility verifier until upstream support catches up

**Workaround (current):**  
This repository currently disables the compatibility verifier in `application.yml` so the application can start while the parent POM and Spring Cloud release train catch up:

```yaml
spring:
  cloud:
    compatibility-verifier:
      enabled: false
```

**Action Required:** Move this repository to a Spring Cloud train that passes the compatibility verifier with Spring Boot 3.5.x, then remove the global `spring.cloud.compatibility-verifier.enabled=false` workaround. The repo is currently pinned to `2024.0.3`, which still requires the verifier workaround at runtime.

---

### 2. Spring Security Filter Chain Conflict

**Problem:**  
Spring Boot 3.5.x has stricter validation for Spring Security filter chains. Multiple `SecurityFilterChain` beans matching "any request" now throw an error:

```text
UnreachableFilterChainException: A filter chain that matches any request
[...ApplicationConfig...] has already been configured, which means that this
filter chain [...ApplicationNoSecurity...] will never get invoked.
```

**Required Fix:**  
Security configurations with overlapping matchers must be mutually exclusive. If using profile-based security configurations:

```java
// Production security config - exclude from test profile
@Configuration
@EnableWebSecurity
@Profile("!test")  // Add this annotation
public class ApplicationConfig {
    // ...
}

// Test security config - only active in test profile
@Configuration
@EnableWebSecurity
@Profile("test")
public class ApplicationNoSecurity {
    // ...
}
```

**Action Required:** This is a downstream project fix, but parent POM documentation should note this breaking change.

---

### 3. Bean Resolution Changes for Inheritance Hierarchies

**Problem:**  
Spring Boot 3.5.x has stricter bean resolution when multiple beans of the same type exist through inheritance:

```text
NoUniqueBeanDefinitionException: expected single matching bean but found 2:
menuService, menuServiceV2
```

**Required Fix:**  
When a service class is extended, the base class should be marked as `@Primary`:

```java
@Service
@Primary  // Add this annotation
public class MenuService {
    // ...
}

@Service
public class MenuServiceV2 extends MenuService {
    // ...
}
```

**Action Required:** This is a downstream project fix, but parent POM documentation should note this breaking change.

---

### 4. Maven Resource Filtering

**Problem:**  
Property placeholders like `@aws.profile.name@` in `application.yml` are not being replaced because Maven resource filtering is not enabled by default.

```text
Profile '@aws.profile.name@' must start and end with a letter or digit
```

**Required Fix in Projects:**  
Enable resource filtering in `pom.xml`:

```xml
<build>
  <resources>
    <resource>
      <directory>src/main/resources</directory>
      <filtering>true</filtering>
    </resource>
  </resources>
  <testResources>
    <testResource>
      <directory>src/test/resources</directory>
      <filtering>true</filtering>
    </testResource>
  </testResources>
</build>
```

**Action Required:** Consider adding this configuration to the parent POM so all child projects inherit it.

---

## Summary of Parent POM Changes Required

### Critical (Must Fix)

1. **Update Spring Cloud version** to be compatible with Spring Boot 3.5.x
   - Current: `2022.0.4`
   - Required: `2024.0.x` or `2025.0.x` when available

### Recommended (Should Add)

1. **Add default resource filtering configuration** so child projects don't need to configure it manually

2. **Update documentation** to note the following breaking changes for downstream projects:
   - Security filter chain mutual exclusivity requirements
   - Bean resolution changes for inheritance hierarchies
   - Profile annotation requirements for conditional configurations

### Temporary Workarounds Applied in stacks-java

Until the parent POM is updated, the following workarounds have been applied:

- Spring Cloud incompatibility: pin the BOM to `2024.0.3` and disable the compatibility verifier globally until an officially compatible train is available. Files: `java/pom.xml`, `java/src/main/resources/application.yml`
- Security filter chain conflict: added `@Profile("!test")`. File: `ApplicationConfig.java`
- Bean resolution conflict: added `@Primary`. File: `MenuService.java`
- Resource filtering: recommended to add filtering config in the parent POM; not yet applied in `java/pom.xml`.

## Testing Verification

After parent POM updates, verify with:

```bash
cd java
./mvnw clean test
./mvnw clean verify -P owasp-dependency-check
```

## References

- [Spring Cloud Release Train Compatibility](https://spring.io/projects/spring-cloud#overview)
- [Spring Boot 3.5 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.5-Release-Notes)
- [Spring Security Filter Chain Configuration](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html)
