# stacks-java [![Build Status](https://dev.azure.com/amido-dev/Amido-Stacks/_apis/build/status/amido-stacks-java-springboot-aks?branchName=master)](https://dev.azure.com/amido-dev/Amido-Stacks/_build/latest?definitionId=101&branchName=master)
Java Web API Application Scaffolding for Amido Stacks

## Code quality

### Formatter

#### Installation
Please install the [intellij-java-google-style.xml](../tools/formatter/intellij-java-google-style.xml) formatter configuration file in the IDE.

#### Usage
The Java source code will automatically be reformatted to comply with [Google Java Style](https://google.github.io/styleguide/javaguide.html). <br /><br />
You can override the settings in the codebase, for example:<br />
```//@formatter:off```<br />
```manually formatted code```<br />
```///@formatter:on````<br />

#### Validate the formatting
`./mvnw com.coveo:fmt-maven-plugin:check`

#### Apply the formatting to the source files
`./mvnw com.coveo:fmt-maven-plugin:format`

#### Validate the source code style
`./mvnw checkstyle:check `

#### Verify that there are no common programming flaws in the byte code
`./mvnw spotbugs:check `

## Check the code test coverage
`./mvnw jacoco:check `

## Generate code coverage report
`./mvnw jacoco:report `
 
## Runs the unit tests of the application and Generate maven test report
 `./mvnw surefire:test `
 
## Run Locally

`./mvnw spring-boot:run`

## Build Docker Image

`docker build -t image-tag .`

## OWASP(Open Web Application Security Project) dependency checker 
Detects publicly disclosed vulnerabilities contained within a Project's 
dependencies.

#### Generate a Dependency vulnerability checker for maven libraries
`mvn clean install -Powasp-dependency-check `

If you have an `.m2` directory in the `java/` folder the Docker build will
attempt to copy the files inside the container and use the cached versions.

## Swagger/OAS

Automatically generated. Go to: http://localhost:9000/swagger/index.html

## Health check

Available at: http://localhost:9000/health
(This can also be configured to run on another port)

## CONFIGURATION

The following environment variables are required:

- AZURE_COSMOSDB_KEY
- AZURE_APPLICATION_INSIGHTS_INSTRUMENTATION_KEY

Speak to a member of the team for keys

## Testing
Set an env variable BASE_URL (e.g. local is http://localhost:9000) 
(from project path: stacks-java/api-tests)
### Run all Tests
`mvn clean verify `

### Run Smoke tests
`mvn clean verify -Dcucumber.options=“--tags @Smoke” `

### Run Functional tests
`mvn clean verify -Dcucumber.options=“--tags @Functional” `

### View The Test Report
`..stacks-java/api-tests/target/site/serenity/index.html`
 
## IDE plugins used for ideal experience (Intellij)
1. SonarLint - for linting
2. CheckStyle - for checking code Style
3. Lombok - to reduce writing boiler plate code(eg: Getter/Setter/equals/canEqual/hashCode/toString)
4. Spring Tools - Spring support for the IDE
5. Spring Assistant - Assist in developing Spring Application

### Maven Wrapper

There are two Maven wrappers in the repo, one in `java/` and one in `api-tests/`.
Both these aid in running maven commands by using `./mvn` instead.
