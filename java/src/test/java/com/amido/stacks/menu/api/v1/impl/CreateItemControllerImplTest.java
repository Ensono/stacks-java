package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Menu;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("Integration")
@ActiveProfiles("test")
class CreateItemControllerImplTest {

  public static final String CREATE_ITEM = "%s/v1/menu/%s/category/%s/items";
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testAddItem() {
    //Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    CreateItemRequest request =
        new CreateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), menu.getId(), category.getId()),
            request,
            ResourceCreatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(CREATED);
  }

  @Test
  void testInvalidCategoryIdWilThrowBadRequest() {
    // Given
    CreateItemRequest request =
        new CreateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_ITEM, getBaseURL(port), randomUUID(), "xyz"),
            request,
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }
}
