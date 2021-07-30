package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Menu;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategories;
import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.ItemHelper.createItem;
import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static com.amido.stacks.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class DeleteCategoryControllerImplTest {

  public static final String DELETE_CATEGORY = "%s/v1/menu/%s/category/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testDeleteCategorySuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteCategoryWithAnItem() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    category.addOrUpdateItem(createItem(0));
    menu.addOrUpdateCategory(category);

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteACategoryFromList() {
    // Given
    Menu menu = createMenu(1);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), categories.get(0).getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }
}
