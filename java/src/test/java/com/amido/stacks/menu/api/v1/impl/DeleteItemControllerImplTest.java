package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.amido.stacks.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static com.amido.stacks.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.OK;

/** @author ArathyKrishna */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class DeleteItemControllerImplTest {

  public static final String DELETE_ITEM = "%s/v1/menu/%s/category/%s/items/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testDeleteItemSuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    Item item = new Item(UUID.randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);

    // When
    String requestUrl =
        String.format(DELETE_ITEM, getBaseURL(port), menu.getId(), category.getId(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ResponseEntity.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }

}
