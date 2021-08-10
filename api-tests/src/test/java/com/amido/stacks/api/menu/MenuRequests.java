package com.amido.stacks.api.menu;

import com.amido.stacks.api.OAuthConfigurations;
import com.amido.stacks.api.WebServiceEndPoints;
import java.util.HashMap;
import java.util.Map;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;

public class MenuRequests {

  private static String menuUrl =
      WebServiceEndPoints.BASE_URL.getUrl().concat(WebServiceEndPoints.MENU.getUrl());
  private static String OAUTH_TOKEN_URL =
      OAuthConfigurations.OAUTH_TOKEN_URL.getOauthConfiguration();
  private static String authorizationToken;

  private static EnvironmentVariables environmentVariables =
      SystemEnvironmentVariables.createEnvironmentVariables();

  private static String generateAuthorisation =
      EnvironmentSpecificConfiguration.from(environmentVariables)
          .getProperty("generate.auth0.token");

  boolean generateToken = Boolean.parseBoolean(generateAuthorisation);
  private static final Map<String, String> commonHeaders = new HashMap<>();

  public MenuRequests() {
    authorizationToken = String.valueOf(Serenity.getCurrentSession().get("Access Token"));

    if (generateToken) {
      commonHeaders.put("Authorization", "Bearer " + authorizationToken);
    }
  }

  public static void getAuthorizationToken(String body) {
    SerenityRest.given().contentType("application/json").body(body).when().post(OAUTH_TOKEN_URL);
  }
}
