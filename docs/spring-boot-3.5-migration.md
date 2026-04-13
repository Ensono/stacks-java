# Spring Boot 3.5.x Migration - Parent POM Requirements

## Overview

The `stacks-modules-parent` baseline used by this repository brings in Spring Boot 3.5.7, which introduces several breaking changes and compatibility issues that need to be addressed either in the parent POM or in downstream projects.

## Issues Identified

### 1. Spring Cloud Version Incompatibility

**Problem:**  
Earlier Spring Cloud release trains are incompatible with Spring Boot 3.5.7.

```text
Spring Boot [3.5.7] is not compatible with this Spring Cloud release train.
Change Spring Boot version to one of the following versions [3.0.x, 3.1.x].
```

**Required Fix in Parent POM or Project Override:**  
Update `spring.cloud.dependencies.version` to a release train that Spring Boot 3.5.x accepts without disabling the verifier:

- Spring Boot 3.0.x / 3.1.x: Spring Cloud 2022.0.x (Kilburn)
- Spring Boot 3.2.x: Spring Cloud 2023.0.x (Leyton)
- Spring Boot 3.3.x / 3.4.x: Spring Cloud 2024.0.x
- Spring Boot 3.5.x: Spring Cloud 2025.0.x

**Implemented project fix:**  
This repository now aligns the Java module to `spring-cloud-dependencies` `2025.0.1`, removes `spring-cloud-starter-bootstrap`, replaces `spring-cloud-starter-config` with `spring-cloud-context`, and keeps the Spring compatibility verifier enabled in both runtime and test configuration.

**Action Required:** Keep the verifier enabled and treat any future incompatibility as a dependency-alignment issue rather than a configuration override.

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

- Implemented in this repository: `2025.0.1`
- Keep future updates on the 3.5.x-compatible Spring Cloud line

### Recommended (Should Add)

1. **Add default resource filtering configuration** so child projects don't need to configure it manually

2. **Update documentation** to note the following breaking changes for downstream projects:
   - Security filter chain mutual exclusivity requirements
   - Bean resolution changes for inheritance hierarchies
   - Profile annotation requirements for conditional configurations

### Project State in stacks-java

The following Spring Boot 3.5.x mitigations now apply in this repository:

- Spring Cloud compatibility: pin the BOM to `2025.0.1`, remove the global verifier override, and retain only `spring-cloud-context` for refresh-scope support. Files: `java/pom.xml`, `java/src/main/resources/application.yml`, `java/src/test/resources/application-test.yml`
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
