package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.amido.stacks.menu.domain.Menu;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static java.util.UUID.fromString;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class CreateMenuControllerImplTest {

  public static final String CREATE_MENU = "/v1/menu";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testCreateNewMenu() {
    // Given
    Menu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            m.getName(), m.getDescription(), UUID.fromString(m.getRestaurantId()), m.getEnabled());

    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ResourceCreatedResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }
}
