
## Run Tests Locally
1. Ensure that the application is running on local: `./mvnw spring-boot:run`

2. Open the 'stacks-java/api-tests' path in the terminal

3. Execute tests by running one of the following commands:

  a. Run all tests: `mvn clean verify`

  b. Run Smoke tests only: `mvn clean verify -Dcucumber.options="--tags @Smoke"`

  c. Run Functional tests only: `mvn clean verify -Dcucumber.options="--tags @Functional"`

  d. Run tests by other tags and ignore tests that contain @Ignore tags:
  `mvn clean verify  verify -Dcucumber.options="--tags ~@Ignore --tags @YourTag"`

## Check the output report
As a result of the test execution - `serenity-maven-plugin` will automatically generate the test report with name - `index.html`.

Test Report Location is: `stacks-java/api-tests/target/site/serenity/index.html`
 
## Manual Aggregation of the Test Report
1. Open the 'stacks-java/api-tests' path in the terminal
2. Execute the `mvn serenity:aggregate` command
 
## Running tests in parallel threads - Using forks

Choosing the right forking strategy and parallel execution settings can have a substantial impact on the memory requirements and the execution time of the build system.
Using multiple forks can be a good alternative to running all tests in a single JVM, and can reduce the risk of certain types of errors. 


Using Forked Test Execution, new JVM processes are spun up to execute the tests, up to a configurable maximum number of processes. This creates better separation between tests, which can improve their reliability. 


Maven is set to spawn new processes by the `forkCount` configuration element, as shown here below. This can either be a number (the maximum number of forks) or a multiplier (the number of forks per CPU). 
The current configuration uses the value of "2", which means 2 forked processes per CPU:
```
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.22.2</version>
    <configuration>
        <parallel>classes</parallel>
        <threadCount>2</threadCount>
        <forkCount>2</forkCount>
    </configuration>
```

#### Note:
The more threads are used, the higher the chances of sporadic, hard-to-reproduce test failures due to timeouts and other related issues. The added performance gain of each thread also tends to drop off for higher numbers of threads. 

## Health check

Available at: http://localhost:9000/health
(This can also be configured to run on another port)

## CONFIGURATION

The following environment variables are required:

- BASE_URL
e.g. http://localhost:9000
