package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static java.util.UUID.fromString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosDbRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class CreateMenuControllerImplTest {

  public static final String CREATE_MENU = "/v1/menu";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepositoryAdapter menuRepositoryAdapter;

  @Test
  void testCreateNewMenu() {
    // Given
    AzureMenu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            m.getName(), m.getDescription(), UUID.fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepositoryAdapter.findAllByRestaurantIdAndName(
            eq(m.getRestaurantId()), eq(m.getName()), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.emptyList()));
    when(menuRepositoryAdapter.save(any(AzureMenu.class))).thenReturn(m);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ResourceCreatedResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void testThrowsErrorOnExists() {
    // Given
    AzureMenu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            m.getName(), m.getDescription(), UUID.fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepositoryAdapter.findAllByRestaurantIdAndName(
            eq(m.getRestaurantId()), eq(m.getName()), any(Pageable.class)))
        .thenReturn(new PageImpl<>(Collections.singletonList(m)));

    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void testWhenDescriptionNotGivenReturnsBadRequest() {
    // Given
    AzureMenu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(m.getName(), "", fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepositoryAdapter.save(any(AzureMenu.class))).thenReturn(m);
    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(Objects.requireNonNull(response.getBody()).getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testWhenNoNameReturnsBadRequest() {
    // Given
    AzureMenu m = createMenu(1);
    CreateMenuRequest request =
        new CreateMenuRequest(
            "", m.getDescription(), fromString(m.getRestaurantId()), m.getEnabled());

    when(menuRepositoryAdapter.save(any(AzureMenu.class))).thenReturn(m);
    // When
    var response =
        this.testRestTemplate.postForEntity(
            getBaseURL(port) + CREATE_MENU, request, ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    then(Objects.requireNonNull(response.getBody()).getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }
}
