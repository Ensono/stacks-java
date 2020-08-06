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

#### Verify that there are no known issues in the source code
`./mvnw spotbugs:check `
 
## Run Locally

`./mvnw spring-boot:run`

## Build Docker Image

`docker build -t image-tag .`

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
