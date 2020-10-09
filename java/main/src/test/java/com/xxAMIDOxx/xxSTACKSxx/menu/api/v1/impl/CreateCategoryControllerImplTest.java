package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.azure.data.cosmos.internal.Utils.randomUUID;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosDbRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class CreateCategoryControllerImplTest {

  public static final String CREATE_CATEGORY = "%s/v1/menu/%s/category";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepositoryAdapter menuRepositoryAdapter;

  @Test
  void testCanNotAddCategoryIfMenuNotPresent() {
    // Given
    UUID menuId = randomUUID();
    when(menuRepositoryAdapter.findById(eq(menuId.toString()))).thenReturn(Optional.empty());

    CreateCategoryRequest request =
        new CreateCategoryRequest("test Category Name", "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), menuId), request, ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testNoDescriptionGivenReturnsBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("test Category Name", "");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), randomUUID()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {description=must not be blank}");
  }

  @Test
  void testInvalidMenuIdWilThrowBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("test Category Name", "");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), "XXXXXX"),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  void testAddCategory() {
    // Given
    Menu menu = createMenu(1);
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepositoryAdapter.save(any(Menu.class))).thenReturn(menu);

    CreateCategoryRequest request =
        new CreateCategoryRequest("test Category Name", "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), menu.getId()),
            request,
            ResourceCreatedResponse.class);

    // Then
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepositoryAdapter, times(1)).save(captor.capture());
    Menu created = captor.getValue();

    then(created.getName()).isEqualTo(menu.getName());
    then(created.getDescription()).isEqualTo(menu.getDescription());
    then(created.getCategories().size()).isEqualTo(1);
    then(created.getCategories().get(0).getName()).isEqualTo(request.getName());
    then(created.getCategories().get(0).getDescription()).isEqualTo(request.getDescription());

    then(response).isNotNull();
    then(response.getBody()).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    then(created.getCategories().get(0).getId()).isEqualTo(response.getBody().getId().toString());
  }

  @Test
  void testCannotAddCategoryWhichAlreadyExists() {
    // Given
    Menu menu = createMenu(1);
    Category category =
        new Category(
            UUID.randomUUID().toString(), "cat name", "cat description", new ArrayList<>());
    menu.addOrUpdateCategory(category);

    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    CreateCategoryRequest request =
        new CreateCategoryRequest(category.getName(), "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), menu.getId()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void testNoNameGivenReturnsBadRequest() {
    // Given
    CreateCategoryRequest request = new CreateCategoryRequest("", "Some description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), randomUUID()),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    then(response.getBody().getDescription())
        .isEqualTo("Invalid Request: {name=must not be blank}");
  }
}