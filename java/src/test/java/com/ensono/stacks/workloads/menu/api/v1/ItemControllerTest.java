package com.ensono.stacks.workloads.menu.api.v1;

import static com.ensono.stacks.workloads.menu.domain.CategoryHelper.createCategory;
import static com.ensono.stacks.workloads.menu.domain.ItemHelper.createItem;
import static com.ensono.stacks.workloads.menu.domain.ItemHelper.createItems;
import static com.ensono.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.ensono.stacks.workloads.util.TestHelper.getBaseURL;
import static com.ensono.stacks.workloads.util.TestHelper.getRequestHttpEntity;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import com.ensono.stacks.core.api.dto.ErrorResponse;
import com.ensono.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.ensono.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.ensono.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.ensono.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.ensono.stacks.workloads.menu.domain.Category;
import com.ensono.stacks.workloads.menu.domain.Item;
import com.ensono.stacks.workloads.menu.domain.Menu;
import com.ensono.stacks.workloads.menu.service.utility.MenuHelperService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
public class ItemControllerTest {

  public static final String CREATE_ITEM = "%s/v1/menu/%s/category/%s/items";

  public static final String UPDATE_ITEM = "%s/v1/menu/%s/category/%s/items/%s";

  public static final String DELETE_ITEM = "%s/v1/menu/%s/category/%s/items/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MenuHelperService menuHelperService;

  @Test
  void testAddItem() {
    // Given
    Menu menu = createMenu(1);
    Category category =
        new Category(randomUUID().toString(), "cat name", "cat description", new ArrayList<>());
    menuHelperService.addOrUpdateCategory(menu, category);

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
    then(response.getBody()).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
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

  @Test
  void testUpdateItemSuccess() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    Item item = createItem(0);
    menuHelperService.addOrUpdateItem(category, item);
    menuHelperService.addOrUpdateCategory(menu, category);

    UpdateItemRequest request =
        new UpdateItemRequest("Some Name", "Some Description", 13.56d, true);

    // When
    String requestUrl =
        String.format(UPDATE_ITEM, getBaseURL(port), menu.getId(), category.getId(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testUpdateItemDescription() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    List<Item> items = createItems(2);
    category.setItems(items);
    menuHelperService.addOrUpdateCategory(menu, category);

    UpdateItemRequest request =
        new UpdateItemRequest(items.get(0).getName(), "Some Description2", 13.56d, true);

    // When
    String requestUrl =
        String.format(
            UPDATE_ITEM, getBaseURL(port), menu.getId(), category.getId(), items.get(0).getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteItemSuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    Item item = new Item(UUID.randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    menuHelperService.addOrUpdateItem(category, item);
    menuHelperService.addOrUpdateCategory(menu, category);

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
