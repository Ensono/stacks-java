package com.amido.stacks.menu.api.v1.impl;

import static com.amido.stacks.menu.domain.MenuHelper.createMenus;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResultItem;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
public class QueryMenuControllerImplTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  final int DEFAULT_PAGE_NUMBER = 1;
  final int DEFAULT_PAGE_SIZE = 20;

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
            .map(DomainToDtoMapper::toSearchMenuResultItem)
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
    menu.addOrUpdateCategory(category);

    MenuDTO expectedResponse = DomainToDtoMapper.toMenuDto(menu);

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
}
