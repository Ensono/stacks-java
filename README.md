# stacks-java [![Build Status](https://dev.azure.com/amido-dev/Amido-Stacks/_apis/build/status/amido-stacks-java-springboot-aks?branchName=master)](https://dev.azure.com/amido-dev/Amido-Stacks/_build/latest?definitionId=101&branchName=master)
Java Web API Application Scaffolding for Amido Stacks

## Overview
This is a sample Java application showcasing
best coding practices and Integrating with Azure and scaffolding for Amido Stacks
This application implemented [Application Insights](https://docs.microsoft.com/en-us/azure/azure-monitor/app/app-insights-overview) for performance management
[Cosmos DB](https://docs.microsoft.com/en-us/azure/cosmos-db/introduction) is the cloud db used.


# Table of Contents

 - [Versioning](#versioning)
 - [Overview](#overview)
 - [Table of Contents](#table-of-contents)
 - Getting Started
   - [Prerequisites](#prerequisites)
   - [Building](#building)
   - [Code quality](#code-quality)
        - Reports
            - [Test reports](#test-reports)
            - [Dependency-Checker](#dependency-check)
   - [Testing](#testing)
   - [Docker](#docker)
 - [IDE Plugins used](#ide-plugins-used)
 - [Swagger/OAS](#swagger-oas)
 - [Health Check](#health-check)

## Versioning

This is the 1.0.0 of the stacks-java

### Getting Started

#### CONFIGURATION

The following environment variables are required(Speak to dev ops team to provide the below keys):
For local environments use Cosmos DB emulator(CosmosDB Emulator has a known fixed key
For AppInsights change the app to not crash if it can't get to AI, and just log to terminal instead).

- AZURE_COSMOSDB_KEY
- AZURE_APPLICATION_INSIGHTS_INSTRUMENTATION_KEY

#### Prerequisites

Must have minimum Java 11 installed.

#### Build and Run Locally

```bash
./mvnw spring-boot:run
```

#### Code quality

##### Formatter

###### Installation
Please install the [intellij-java-google-style.xml](https://github.com/amido/stacks-java/blob/master/tools/formatter/intellij-java-google-style.xml) formatter configuration file in the IDE.

###### Java google style Usage
The Java source code will automatically be reformatted to comply with [Google Java Style](https://google.github.io/styleguide/javaguide.html). <br /><br />
You can override the settings in the codebase, for example:<br />

```//@formatter:off```<br />
```manually formatted code```<br />
```///@formatter:on```<br />

###### Validate the formatting

```bash
./mvnw com.coveo:fmt-maven-plugin:check
```

###### Apply the formatting to the source files

```bash
./mvnw com.coveo:fmt-maven-plugin:format
```

###### Validate the source code style

```bash
./mvnw checkstyle:check
```

###### Verify that there are no common programming flaws in the byte code

```bash
./mvnw spotbugs:check
```

##### Reports

###### Test reports

####### Generate code coverage report

```bash
./mvnw jacoco:report
```

Generated report can be viewed under – target/site/jacoco/index.html
####### Runs the unit tests of the application and Generate maven test report
Surefire creates a reports in 2 formats. Xml and HTML.

 ```bash
 ./mvnw surefire:test
 ```
 
####### View The Test Report

```bash
..stacks-java/api-tests/target/site/serenity/index.html
```

####### Generate mutation Tests using Pit Test
The mutation coverage goal analyses all classes in the codebase that match the target tests and target classes filters.

```bash
mvn org.pitest:pitest-maven:mutationCoverage
```

Generated report can be viewed under – target/pit-reports/YYYYMMDDHHMI
 
#### Docker
 
##### Build Docker Image

```bash
docker build -t image-tag .
```

###### Dependency-Checker

####### OWASP(Open Web Application Security Project) dependency checker 
Detects publicly disclosed vulnerabilities contained within a Project's 
dependencies.

####### Generate a Dependency vulnerability checker for maven libraries

```bash
mvn clean install -Powasp-dependency-check
```

Generated report can be viewed under – target/dependency-check.html

###### View generated Dependency Checker report
Dependency checker creates a Html folder inside the target folder 
the file name is dependency-check-report.html

If you have an `.m2` directory in the `java/` folder the Docker build will
attempt to copy the files inside the container and use the cached versions.

### Swagger/OAS

Automatically generated. Go to: <http://localhost:9000/swagger/index.html>

### Health check

Available at: <http://localhost:9000/health>
(This can also be configured to run on another port)

#### Testing

Set an env variable BASE_URL (e.g. local is <http://localhost:9000>) 
(from project path: stacks-java/api-tests)

##### Run all Tests

```bash
mvn clean verify
```

##### Run Smoke tests

```bash
mvn clean verify -Dcucumber.options=“--tags @Smoke”
```

##### Run Functional tests

```bash
mvn clean verify -Dcucumber.options=“--tags @Functional”
```
 
### IDE plugins
 
Suggests installing the below plugins for ideal experience (Intellij)
1. SonarLint - for linting
2. CheckStyle - for checking code Style
3. Lombok - to reduce writing boiler plate code(eg: Getter/Setter/equals/canEqual/hashCode/toString)
4. Spring Tools - Spring support for the IDE
5. Spring Assistant - Assist in developing Spring Application

### Maven Wrapper

There are two Maven wrappers in the repo, one in `java/` and one in `api-tests/`.
Both these aid in running maven commands by using `./mvn` instead.
