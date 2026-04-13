package com.amido.stacks.tests.api.stepdefinitions;

import com.amido.stacks.tests.api.menu.MenuActions;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import net.serenitybdd.cucumber.suiteslicing.SerenityTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

  private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);
  private static final String RELAX_HTTPS_VALIDATION_PROPERTY = "relax.https.validation";
  private static final String RELAX_HTTPS_VALIDATION_ENV = "RELAX_HTTPS_VALIDATION";
  private static boolean firstTestRun = false;

  @Before
  public void before() {
    SerenityTags.create().tagScenarioWithBatchingInfo();
  }

  @Before
  public static void beforeAll() {
    if (!firstTestRun) {
      configureHttpsValidation();
      LOGGER.info("Get the Authorization Token");
      MenuActions.getAuthToken();

      firstTestRun = true;
    }
  }

  private static void configureHttpsValidation() {
    if (isRelaxedHttpsValidationEnabled()) {
      LOGGER.warn(
          "Relaxed HTTPS validation enabled for API tests via '{}' or '{}'",
          RELAX_HTTPS_VALIDATION_PROPERTY,
          RELAX_HTTPS_VALIDATION_ENV);
      RestAssured.useRelaxedHTTPSValidation();
    }
  }

  private static boolean isRelaxedHttpsValidationEnabled() {
    String configuredValue = System.getProperty(RELAX_HTTPS_VALIDATION_PROPERTY);

    if (configuredValue == null || configuredValue.isBlank()) {
      configuredValue = System.getenv(RELAX_HTTPS_VALIDATION_ENV);
    }

    return Boolean.parseBoolean(configuredValue);
  }
}
