
## Run Karate Tests
1. Ensure that the application is up and running, e.g. on local: `./mvnw spring-boot:run`
2. Open the 'api-tests-karate' module path in the terminal
3. Set the 'BASE_URL' and Access Token Configurations values in environment variables or in ``karate-config.js``
4. Execute tests by running one of the following commands:

  a. Run Smoke tests: `mvn test -Dtest=RunSmokeTests`
  b. Run Functional tests: `mvn test -Dtest=RunFunctionalTests`
  b. Run Functional tests on the specific environment: `mvn test -Dtest=RunFunctionalTests -Dkarate.env=local`

### Run the specific feature by tag
```
    @Karate.Test
    Karate testFullPath() {
        return Karate.run("classpath:PATH_TO_THE_FEATURE/FEATURE_NAME.feature").tags("@TAG");
    }
```
#### Note: 
 Any *.feature file tagged as @ignore will be skipped - as the ~ prefix means a "NOT" operation. 
 You can also specify tags on the command-line. The tags() method from test runner class also takes multiple arguments,for e.g. 
 `this is an "AND" operation: tags("@customer", "@smoke", "~@ignoredTags")`
 `and this is an "OR" operation: tags("@customer, @smoke, ~@ignoredTags")`



### Switching the Environment
There is only one thing you need to do to switch the environment - which is to set a Java system property.
By default, the value of karate.env when you access it within karate-config.js - is base_url value taken from the environment variables


## Check the output report
As a result of the test execution - `karate` will automatically generate the test report with the name - `karate-summary.html`.

Test Report Location is: `target/surefire-reports/karate-summary.html`
 
 
## Running tests in parallel
Choosing the right forking strategy and parallel execution settings can have a substantial impact on the memory requirements and the execution time of the build system.
The tests can be easily grouped in features and tags to run and compose test-suites in a very flexible manner.

#### Note:
The more threads are used, the higher the chances of sporadic, hard-to-reproduce test failures due to timeouts, test data clean up, and other related issues. 
The added performance gain of each thread also tends to drop off for higher numbers of threads. 

```
class Runner {
    @Test
    void run() {
        Results results = Runner.path("classpath:PATH_TO_FUTURE_OR_PACKAGE").tags("~@ignoredTags", "@YourTag").parallel(1); 
        assertEquals(0, results.getFailCount(), results.getErrorMessages()); 
    }
}
```

#### @parallel=false
In rare cases you may want to suppress the default of Scenario-s executing in parallel and the special tag @parallel=false can be used. If you place it above the Feature keyword, it will apply to all Scenario-s. And if you just want one or two Scenario-s to NOT run in parallel, you can place this tag above only those Scenario-s.

### Parallel Stats
For convenience, some stats are logged to the console when execution completes, which should look something like this:
```
======================================================
elapsed:   2.35 | threads:    5 | thread time: 4.98 
features:    54 | ignored:   25 | efficiency: 0.42
scenarios:  145 | passed:   145 | failed: 0
======================================================
```

## Health check
Available at: http://localhost:9000/health
(This can also be configured to run on another port)

## Configuration
The following environment variables are required:
- BASE_URL (e.g. http://localhost:9000) and the Access Token Configuration

### Access token configuration
In order to make the authenticated requests (pass Bearer Token into headers) for the functional tests, please change the value of ```generate_auth0_token``` from ``karate-config.js`` file config to 'true'.
Also, to get the right access token please provide values for the following variables in the ``environment variables``:

```
- CLIENT_ID
- CLIENT_SECRET
- AUDIENCE
- GRANT_TYPE
- OAUTH_TOKEN_URL
```
###### Note: In case the Authorization Token is not required the ```generate_auth0_token``` value is false