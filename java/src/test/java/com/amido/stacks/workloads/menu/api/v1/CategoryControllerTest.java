package com.amido.stacks.workloads.menu.api.v1;

import static com.amido.stacks.workloads.menu.domain.CategoryHelper.createCategories;
import static com.amido.stacks.workloads.menu.domain.CategoryHelper.createCategory;
import static com.amido.stacks.workloads.menu.domain.ItemHelper.createItem;
import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.workloads.util.TestHelper.getBaseURL;
import static com.amido.stacks.workloads.util.TestHelper.getRequestHttpEntity;
import static java.util.UUID.fromString;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.Application;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.menu.service.data.MenuQueryService;
import com.amido.stacks.workloads.menu.service.v1.CategoryService;
import com.amido.stacks.workloads.menu.service.v1.utility.MenuHelperService;
import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosHealthConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {Application.class})
@EnableAutoConfiguration(
    exclude = {
      CosmosRepositoriesAutoConfiguration.class,
      CosmosAutoConfiguration.class,
      CosmosHealthConfiguration.class
    })
@Tag("Integration")
@ActiveProfiles("test")
public class CategoryControllerTest {

  public static final String CREATE_CATEGORY = "%s/v1/menu/%s/category";

  public static final String UPDATE_CATEGORY = "%s/v1/menu/%s/category/%s";

  public static final String DELETE_CATEGORY = "%s/v1/menu/%s/category/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private CategoryService categoryService;

  @Autowired private MenuHelperService menuHelperService;

  @MockBean private MenuRepository menuRepository;

  @MockBean private MenuQueryService menuQueryService;

  @Test
  void testInvalidMenuIdWillThrowBadRequest() {
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

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

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

  @Test
  void testUpdateCategorySuccess() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menuHelperService.addOrUpdateCategory(menu, category);

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(category.getId()));

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
  void testUpdateCategoryFailure() {
    // Given
    Menu menu = createMenu(0);
    Category category = createCategory(0);
    menuHelperService.addOrUpdateCategory(menu, category);

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.empty());
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(category.getId()));

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testUpdateCategoryWhenMultipleCategoriesExists() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);

    UpdateCategoryRequest request = new UpdateCategoryRequest("new Category", "new Description");

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    // When
    String requestUrl =
        String.format(UPDATE_CATEGORY, getBaseURL(port), menu.getId(), categories.get(0).getId());

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
  void testUpdateOnlyCategoryDescription() {
    // Given
    Menu menu = createMenu(0);
    List<Category> categoryList = createCategories(2);
    menu.setCategories(categoryList);

    UpdateCategoryRequest request =
        new UpdateCategoryRequest(categoryList.get(1).getName(), "new Description");

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);

    // When
    String requestUrl =
        String.format(
            UPDATE_CATEGORY,
            getBaseURL(port),
            fromString(menu.getId()),
            fromString(categoryList.get(1).getId()));
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);
  }

  @Test
  void testDeleteCategorySuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    List<Category> categories = new ArrayList<>();
    categories.add(category);
    menu.setCategories(categories);

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    doNothing().when(menuRepository).delete(any(Menu.class));

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
    menuHelperService.addOrUpdateItem(category, createItem(0));
    menuHelperService.addOrUpdateCategory(menu, category);

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    doNothing().when(menuRepository).delete(any(Menu.class));

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

    when(menuQueryService.findById(UUID.fromString(menu.getId()))).thenReturn(Optional.of(menu));
    doNothing().when(menuRepository).delete(any(Menu.class));

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
