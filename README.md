# stacks-java [![Build Status](https://dev.azure.com/amido-dev/Amido-Stacks/_apis/build/status/amido-stacks-java-springboot-aks?branchName=master)](https://dev.azure.com/amido-dev/Amido-Stacks/_build/latest?definitionId=101&branchName=master)
Java Web API Application Scaffolding for Amido Stacks

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

### Maven Wrapper

There are two Maven wrappers in the repo, one in `java/` and one in `api-tests/`.
Both these aid in running maven commands by using `./mvn` instead.
