package com.amido.stacks.workloads.menu.api.v1;

import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenus;
import static com.amido.stacks.workloads.util.TestHelper.getBaseURL;
import static com.amido.stacks.workloads.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResultItem;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.CategoryMapper;
import com.amido.stacks.workloads.menu.mappers.ItemMapper;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.service.utility.MenuHelperService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class MenuControllerTest {

  public static final String CREATE_MENU = "/v1/menu";

  public static final String UPDATE_MENU = "%s/v1/menu/%s";

  public static final String DELETE_MENU = "%s/v1/menu/%s";

  final int DEFAULT_PAGE_NUMBER = 1;
  final int DEFAULT_PAGE_SIZE = 20;

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MenuMapper menuMapper;

  @Autowired private CategoryMapper categoryMapper;

  @Autowired private ItemMapper itemMapper;

  @Autowired private SearchMenuResultItemMapper searchMenuResultItemMapper;

  @Autowired private MenuHelperService menuHelperService;

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

  @Test
  public void listMenusAndPagination() {

    // Given

    int pageNumber = 5;
    int pageSize = 6;

    // When
    var response =
        this.testRestTemplate.getForEntity(
            getBaseURL(port)
                + String.format("/v1/menu?pageSize=%d&pageNumber=%d", pageSize, pageNumber),
            SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(pageNumber));
    assertThat(actual.getPageSize(), is(pageSize));
  }

  @Test
  public void listMenusFilteredByRestaurantId() {

    // Given
    final String menuId = "d5785e28-306b-4e23-add0-3f9092d395f8";
    final String restaurantId = "58a1df85-6bdc-412a-a118-0f0e394c1342";

    List<Menu> menuList = createMenus(3);
    Menu match = new Menu(menuId, restaurantId, "name", "description", new ArrayList<>(), true);
    match.setRestaurantId(restaurantId);
    menuList.add(match);
    List<Menu> matching = Collections.singletonList(match);

    List<SearchMenuResultItem> expectedMenuList =
        matching.stream()
            .map(m -> searchMenuResultItemMapper.toDto(m))
            .collect(Collectors.toList());

    SearchMenuResult expectedResponse =
        new SearchMenuResult(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, expectedMenuList);

    // When
    var result =
        this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu?restaurantId=%s", getBaseURL(port), restaurantId),
            SearchMenuResult.class);
    // Then
    then(result.getBody().getPageNumber()).isEqualTo(expectedResponse.getPageNumber());
    then(result.getBody().getPageSize()).isEqualTo(expectedResponse.getPageSize());
    then(result.getBody().getResults()).containsAll(expectedResponse.getResults());
  }

  @Test
  public void getMenuById() {
    // Given
    final String menuId = "d5785e28-306b-4e23-add0-3f9092d395f8";
    final String restaurantId = "58a1df85-6bdc-412a-a118-0f0e394c1342";
    final String categoryId = "2c43dbda-7d4d-46fb-b246-bec2bd348ca1";
    final String itemId = "7e46a698-080b-45e6-a529-2c196d00791c";

    Menu menu = new Menu(menuId, restaurantId, "name", "description", new ArrayList<>(), true);
    Item item = new Item(itemId, "item name", "item description", 5.99d, true);
    Category category =
        new Category(categoryId, "cat name", "cat description", Arrays.asList(item));
    menuHelperService.addOrUpdateCategory(menu, category);

    MenuDTO expectedResponse = menuMapper.toDto(menu);

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu/%s", getBaseURL(port), menu.getId()), MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }

  @Test
  public void listMenusWithDefaultPagination() {

    // When
    var response =
        this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu", SearchMenuResult.class);

    // Then
    then(response.getBody()).isInstanceOf(SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(DEFAULT_PAGE_NUMBER));
    assertThat(actual.getPageSize(), is(DEFAULT_PAGE_SIZE));
  }

  @Test
  void testUpdateSuccess() {
    // Given
    Menu menu = createMenu(0);

    UpdateMenuRequest request = new UpdateMenuRequest("new name", "new description", false);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, getBaseURL(port), menu.getId()),
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testDeleteMenuSuccess() {
    // Given
    Menu menu = createMenu(1);

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, getBaseURL(port), menu.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ResponseEntity.class);
    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }
}
