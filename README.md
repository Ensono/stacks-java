jThe full documentation on Amido Stacks can be found [here](https://amido.github.io/stacks/).

## Using the repository to build the Java Spring Boot Rest API application

### Customised solution

To customise the namespaces and create an application for your company please visit the full [documentation](https://stacks.amido.com/docs/)

### Running the application locally

1. Clone one of the Java projects to your local machine from one of the following repos:
   ---
    1. Simple web API: [stacks-java repository](https://github.com/amido/stacks-java)
    2. Web API with CQRS: [stacks-java-cqrs repository](https://github.com/amido/stacks-java-cqrs)
    3. Web API with CQRS and events: [stacks-java-cqrs-events repository](https://github.com/amido/stacks-java-cqrs-events)

2. Build and run the application
   ---

   Note that at a minimum [Java 11](https://adoptopenjdk.net/) should be installed.

   Move to the `<PROJECT-NAME>/java` folder, then

   For Unix:

    ```bash
    ./mvnw spring-boot:run
    ```

   For Windows:

    ```bash
    mvnw.cmd spring-boot:run
    ```

   For instructions on how to customise the project for your company please look in the [Scaffolding](docs/workloads/azure/backend/java/scaffolding_java.md) section
3. Verify that the application has started
   ---
   Browse to [http://localhost:9000/v1/menu](http://localhost:9000/v1/menu). This should return a valid JSON response.

   The application configuration uses Swagger/OAS3 to represent the API endpoints. The Swagger UI can be viewed by directing your
   browser to [http://localhost:9000/swagger/index.html](http://localhost:9000/swagger/index.html).
   
   **Note**: This version of the application doesn't use any persistence, so the responses are mocked 

### Authorization

All API endpoints are (optionally) protected using **Auth0**. There is an `auth.properties` file within the project codebase.
If the following property within this file is set:

```text
auth.isEnabled=true
```

then clients will need to pass an `Authorization` header containing the Bearer token generated from Auth0 as part of the endpoint request. If the value
is set to `false` then no authorization is required.

#### Auth0 configuration properties

If using Auth0 for authorization, Auth0 itself will need to be configured with both an API definition and an associated Application.
There are corresponding configuration values required for the Stacks application, within the `auth.properties` file, e.g.

```text
auth0.issuer=https://amidostacks.eu.auth0.com/
auth0.apiAudience=https://amidostacks.eu.auth0.com/api/v2/
```

These parameters are used to verify that the JWT supplied in the Authorization header of a request is valid.

#### Swagger/OAS

- Automatically generated for the project. Go to [Swagger Index](http://localhost:9000/swagger/index.html) to view.
- Swagger Json is here: [Swagger Json](http://localhost:9000/swagger/oas.json)

#### Health check

- Available at: [health check](http://localhost:9000/health)
  (This can also be configured to run on another port)

## Using a Docker image

<https://docs.docker.com/docker-for-windows/install/>

From the `<PROJECT-NAME>/java` folder, build a Docker image using e.g. the command below:

   ```bash
   docker build --tag stacks:1.0 .
   ```

This uses the `Dockerfile` in this folder to generate the Docker image.

If you have an `.m2` directory in the `java/` folder, the Docker build will attempt to copy the files inside the container and use the cached versions.

Once the Docker image is created, you can then run a Docker container based on this image using e.g.

   ```bash
   docker run -p 9000:9000 -e AZURE_APPLICATION_INSIGHTS_INSTRUMENTATION_KEY -e AZURE_COSMOSDB_KEY stacks:1.0
   ```

which passes in the two required environment variables from your own environment.
