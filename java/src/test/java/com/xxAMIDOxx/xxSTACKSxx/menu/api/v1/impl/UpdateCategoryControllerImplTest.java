package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.azure.data.cosmos.internal.Utils.randomUUID;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategories;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Integration")
class UpdateCategoryControllerImplTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @MockBean
  private MenuRepository menuRepository;

  @Test
  void testUpdateSuccess() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request =
            new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl = String.format("%s/v1/menu/%s/category/%s",
            getBaseURL(port), UUID.fromString(menu.getId()), UUID.fromString(category.getId()));
    var response = this.testRestTemplate.exchange(requestUrl, HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()), ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);

    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepository, times(1)).save(captor.capture());
    Menu updated = captor.getValue();
    then(updated.getCategories()).hasSize(1);
    Category updatedCategory = updated.getCategories().get(0);
    then(updatedCategory.getDescription()).isEqualTo(request.getDescription());
    then(updatedCategory.getName()).isEqualTo(request.getName());
  }

  @Test
  void testCannotUpdateCategoryIfNoMenuExists() {
    // Given
    UUID menuId = randomUUID();
    when(menuRepository.findById(eq(menuId.toString()))).thenReturn(Optional.empty());

    UpdateCategoryRequest request =
            new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl = String.format("%s/v1/menu/%s/category/%s",
            getBaseURL(port), menuId, randomUUID());
    var response =
            this.testRestTemplate.exchange(requestUrl, HttpMethod.PUT,
                    new HttpEntity<>(request, getRequestHttpEntity()), ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testCannotUpdateCategoryIfNotAlreadyExists() {
    // Given
    Menu menu = createMenu(0);
    menu.setCategories(null);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request =
            new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl = String.format("%s/v1/menu/%s/category/%s",
            getBaseURL(port), UUID.fromString(menu.getId()), UUID.randomUUID());
    var response = this.testRestTemplate.exchange(requestUrl, HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()), ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testCannotUpdateCategoryIfNameAlreadyExists() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categoryList = createCategories(2);
    categoryList.get(0).setName("new Category");

    menu.setCategories(categoryList);
    when(menuRepository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    UpdateCategoryRequest request =
            new UpdateCategoryRequest("new Category", "new Description");

    // When
    String requestUrl = String.format("%s/v1/menu/%s/category/%s",
            getBaseURL(port), UUID.fromString(menu.getId()), UUID.fromString(categoryList.get(1).getId()));
    var response =
            this.testRestTemplate.exchange(requestUrl, HttpMethod.PUT,
                    new HttpEntity<>(request, getRequestHttpEntity()), ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(CONFLICT);
  }
}