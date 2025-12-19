## Plan: Fix Cucumber Test Discovery with Modern Serenity-Cucumber Configuration

The CI/CD pipeline fails because `CucumberTestSuite` uses deprecated `@SelectClasspathResource` and is missing glue configuration. The fix requires migrating to `@ConfigurationParameter`-based configuration, adding `cucumber.properties`, upgrading to Serenity 4.2.26 + Cucumber 7.22.2, and adding a local helper script for Cucumber tag translation.

### Steps

1. **Update [`CucumberTestSuite.java`](api-tests/src/test/java/com/amido/stacks/tests/api/CucumberTestSuite.java)**: Replace `@SelectClasspathResource("/cucumber/features")` with `@ConfigurationParameter` annotations for `FEATURES_PROPERTY_NAME` (`classpath:cucumber/features`), `GLUE_PROPERTY_NAME` (`com.amido.stacks.tests.api.stepdefinitions`), `FILTER_TAGS_PROPERTY_NAME` (`@Functional or @Smoke or @Performance and not @Ignore`), and update `PLUGIN_PROPERTY_NAME` to use `SerenityReporterParallel`.

2. **Create [`cucumber.properties`](api-tests/src/test/resources/cucumber.properties)**: Add `cucumber.features=classpath:cucumber/features`, `cucumber.glue=com.amido.stacks.tests.api.stepdefinitions`, `cucumber.filter.tags=@Functional or @Smoke or @Performance and not @Ignore`, `cucumber.plugin=io.cucumber.core.plugin.SerenityReporterParallel,pretty`, `cucumber.publish.quiet=true`, and `cucumber.execution.parallel.enabled=true`.

3. **Upgrade versions in [`api-tests/pom.xml`](api-tests/pom.xml)**: Change `<serenity.version>` to `4.2.26`, `<serenity.maven.version>` to `4.2.26`, and `<cucumber.version>` to `7.22.2`.

4. **Update [`api-tests/pom.xml`](api-tests/pom.xml) failsafe plugin**: Add `<cucumber.filter.tags>${cucumber.filter.tags}</cucumber.filter.tags>` to `<systemPropertyVariables>` to allow CI/CD override.

5. **Create local helper script [`build/azDevOps/azure/scripts/convert-junit-tags-to-cucumber.bash`](build/azDevOps/azure/scripts/convert-junit-tags-to-cucumber.bash)**: Write a bash script that takes JUnit 5 tag format (`Functional | Smoke | Performance`) and converts to Cucumber format (`@Functional or @Smoke or @Performance`). Handle ignored tags by appending `and not @Ignore`.

6. **Create local template override [`build/azDevOps/azure/templates/steps/deploy/deploy-post-deploy-tests-cucumber.yml`](build/azDevOps/azure/templates/steps/deploy/deploy-post-deploy-tests-cucumber.yml)**: Wrap the upstream `deploy-post-deploy-tests.yml` template, calling the helper script first to set `cucumber.filter.tags` environment variable, then passing it to Maven via `-Dcucumber.filter.tags`.

7. **Update [`azure-pipelines-javaspring-k8s.yml`](build/azDevOps/azure/azure-pipelines-javaspring-k8s.yml)**: Replace references to `deploy-post-deploy-tests.yml` with the new local `deploy-post-deploy-tests-cucumber.yml` template for Dev and Prod stages.

8. **Run local verification before commit**:
   - Execute `./mvnw verify -f api-tests/pom.xml` to confirm Cucumber tests are discovered
   - Execute `./mvnw verify -f api-tests/pom.xml -Dcucumber.filter.tags="@Smoke"` to verify tag filtering works
   - Execute `./mvnw verify -f java/pom.xml` to ensure main project still builds
   - Run the helper script manually: `bash build/azDevOps/azure/scripts/convert-junit-tags-to-cucumber.bash "Functional | Smoke" "Ignore"` to verify output

### Further Considerations

1. **Template structure**: The new local template should invoke the helper script in a `Bash@3` task before calling the upstream template, storing the converted tags in a pipeline variable that gets passed through.

2. **Fallback behavior**: If `cucumber.filter.tags` is not provided via command line, the `cucumber.properties` file provides sensible defaults. This ensures local developer runs work without needing to specify tags.

Please ensure that you do not disable Commit Signing in your Git client when committing these changes, this is a mandatory requirement. You may disable signing of the build locally to run unattended as long as it is signed in the CI/CD pipeline.
