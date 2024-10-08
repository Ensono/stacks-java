package com.amido.stacks.tests.api.menu;

import static net.serenitybdd.rest.SerenityRest.lastResponse;

import com.amido.stacks.tests.api.OAuthConfigurations;
import com.amido.stacks.tests.api.models.AuthorizationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuActions {

  private static final Logger LOGGER = LoggerFactory.getLogger(MenuActions.class);
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static final String client_id = OAuthConfigurations.CLIENT_ID.getOauthConfiguration();
  private static final String client_secret =
      OAuthConfigurations.CLIENT_SECRET.getOauthConfiguration();
  private static final String audience = OAuthConfigurations.AUDIENCE.getOauthConfiguration();
  private static final String grant_type = OAuthConfigurations.GRANT_TYPE.getOauthConfiguration();
  private static final EnvironmentVariables environmentVariables =
      SystemEnvironmentVariables.createEnvironmentVariables();
  private static final String generateAuthorisation =
      EnvironmentSpecificConfiguration.from(environmentVariables)
          .getProperty("generate.auth0.token");
  private static String authBody;

  static {
    try {
      authBody =
          objectMapper.writeValueAsString(
              new AuthorizationRequest(client_id, client_secret, audience, grant_type));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public static void getAuthToken() {
    boolean generateToken = Boolean.parseBoolean(generateAuthorisation);

    if (generateToken) {
      MenuRequests.getAuthorizationToken(authBody);
      String accessToken = lastResponse().jsonPath().get("access_token").toString();
      Serenity.setSessionVariable("Access Token").to(accessToken);

      if (accessToken.isEmpty()) {
        LOGGER.error("The access token could not be obtained");
      }
    } else {
      Serenity.setSessionVariable("Access Token").to("");
    }
  }
}
