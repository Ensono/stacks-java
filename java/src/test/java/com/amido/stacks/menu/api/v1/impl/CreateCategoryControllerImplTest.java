package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.amido.stacks.menu.domain.Menu;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class CreateCategoryControllerImplTest {

  public static final String CREATE_CATEGORY = "%s/v1/menu/%s/category";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

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

    CreateCategoryRequest request =
        new CreateCategoryRequest("test Category Name", "test Category Description");

    // When
    var response =
        this.testRestTemplate.postForEntity(
            String.format(CREATE_CATEGORY, getBaseURL(port), menu.getId()),
            request,
            ResourceCreatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getBody()).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }
}
