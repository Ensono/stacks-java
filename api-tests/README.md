
## Run Tests Locally
1. Ensure that the application is running on local: `./mvnw spring-boot:run`

2. Open the 'stacks-java/api-tests' path in the terminal

3. Execute tests by run one of the following commands:

  a. Run all tests: `mvn clean verify`

  b. Run Smoke tests only: `mvn clean verify -Dcucumber.options="--tags @Smoke"`

  c. Run Functional tests only: `mvn clean verify -Dcucumber.options="--tags @Functional"`

  d. Run tests by other tags and ignore tests that contain @Ignore tags:
  `mvn clean verify  verify -Dcucumber.options="--tags ~@Ignore --tags @YourTag"`

## Check the output report
Please use this path to find the generated test report:

 `stacks-java/api-tests/target/site/serenity/index.html`

## Health check

Available at: http://localhost:9000/health
(This can also be configured to run on another port)

## CONFIGURATION

The following environment variables are required:

- BASE_URL
e.g. http://localhost:9000
