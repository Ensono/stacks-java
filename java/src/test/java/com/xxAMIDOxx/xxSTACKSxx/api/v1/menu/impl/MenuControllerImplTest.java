package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.CreateCategoryRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.MenuCreateRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto.ResourceCreatedResponseDto;
import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xxAMIDOxx.xxSTACKSxx.model.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.model.MenuHelper.createMenus;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Integration")
class MenuControllerImplTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @MockBean
  private MenuRepository menuRepository;

  @Mock
  Menu menu;

  @Mock
  Category category;

  final int DEFAULT_PAGE_NUMBER = 1;
  final int DEFAULT_PAGE_SIZE = 20;

  @BeforeEach
  void init_mocks() {
    MockitoAnnotations.initMocks(this);
    menu = new Menu();
    menu.setId(randomUUID().toString());
    menu.setEnabled(true);
    menu.setName("testMenu");
    menu.setRestaurantId(UUID.randomUUID());
    menu.setDescription("something");

    category = new Category();
    category.setName("test Category");
    category.setDescription("test Category Description");
  }


  @Test
  void listMenusAndPagination() {

    // Given
    when(
            menuRepository.findAll(any(Pageable.class))
    ).thenReturn(new PageImpl<>(createMenus(1)));

    int pageNumber = 5;
    int pageSize = 6;

    // When
    var response = this.testRestTemplate.getForEntity(
            getBaseURL(port) + String.format("/v1/menu?pageSize=%d&pageNumber=%d",
                    pageSize, pageNumber), SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();

    // Then
    verify(menuRepository, times(1)).findAll(any(Pageable.class));
    then(response.getStatusCode()).isEqualTo(OK);
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(pageNumber));
    assertThat(actual.getPageSize(), is(pageSize));
  }

  @Test
  void listMenusFilteredByRestaurantId() {

    // Given
    final UUID restaurantId = randomUUID();

    List<Menu> menuList = createMenus(3);
    Menu match = menuList.get(0);
    match.setRestaurantId(restaurantId);
    menuList.add(match);
    List<Menu> matching = Collections.singletonList(match);

    List<SearchMenuResultItem> expectedMenuList = matching.stream()
            .map(SearchMenuResultItem::new)
            .collect(Collectors.toList());

    SearchMenuResult expectedResponse = new SearchMenuResult(
            DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, expectedMenuList);

    when(
            menuRepository.findAllByRestaurantId(
                    eq(restaurantId.toString()),
                    any(Pageable.class))
    ).thenReturn(new PageImpl<>(matching));

    // When
    var result = this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu?restaurantId=%s", getBaseURL(port), restaurantId),
            SearchMenuResult.class);
    // Then
    then(result.getBody().getPageNumber().equals(expectedResponse.getPageNumber()));
    then(result.getBody().getPageSize().equals(expectedResponse.getPageSize()));
    then(result.getBody().getResults().containsAll(expectedResponse.getResults()));
  }

  @Test
  void listMenusFilteredByRestaurantIdAndSearchTerm() {
    // Given
    final UUID restaurantId = randomUUID();
    final String searchTerm = "searchTermString";

    when(
            menuRepository.findAllByRestaurantIdAndNameContaining(
                    eq(restaurantId.toString()),
                    eq(searchTerm),
                    any(Pageable.class))
    ).thenReturn(new PageImpl<>(Collections.emptyList()));

    // When
    this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu?restaurantId=%s&searchTerm=%s",
                    getBaseURL(port), restaurantId, searchTerm),
            SearchMenuResult.class);
    // Then
    verify(menuRepository, times(1))
            .findAllByRestaurantIdAndNameContaining(eq(restaurantId.toString()),
                    eq(searchTerm),
                    any(Pageable.class));
  }

  @Test
  void listMenusFilteredBySearchTerm() {
    // Given
    final String searchTerm = "searchTermString";

    when(
            menuRepository.findAllByNameContaining(
                    eq(searchTerm),
                    any(Pageable.class))
    ).thenReturn(new PageImpl<>(createMenus(0)));

    // When
    this.testRestTemplate.getForEntity(
            String.format("%s/v1/menu?searchTerm=%s",
                    getBaseURL(port), searchTerm),
            SearchMenuResult.class);
    // Then
    verify(menuRepository, times(1))
            .findAllByNameContaining(eq(searchTerm), any(Pageable.class));
  }

  @Test
  void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    when(menuRepository.findById(menu.getId())).thenReturn(Optional.of(menu));

    // When
    var response = this.testRestTemplate.getForEntity(
            getBaseURL(port) + "/v1/menu/" + menu.getId(),
            Menu.class);

    // Then
    then(response.getBody()).isEqualTo(menu);
  }

  @Test
  void listMenusWithDefaultPagination() {
    // Given
    when(
            menuRepository.findAll(any(Pageable.class))
    ).thenReturn(new PageImpl<>(createMenus(1)));

    // When
    var response =
            this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu",
                    SearchMenuResult.class);

    // Then
    then(response.getBody()).isInstanceOf(SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(DEFAULT_PAGE_NUMBER));
    assertThat(actual.getPageSize(), is(DEFAULT_PAGE_SIZE));
  }

  @Test
  void testCreateNewMenu() {
    // Given
    MenuCreateRequestDto dto = new MenuCreateRequestDto();
    dto.setDescription("TestDto");
    dto.setEnabled(true);
    dto.setName("Test1");
    dto.setTenantId(UUID.randomUUID().toString());
    when(menuRepository.save(any(Menu.class)))
            .thenReturn(new Menu());
    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) + "/v1/menu", dto, ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  void testWhenNotAllFieldsGivenReturnsBadRequest() {
    // Given
    MenuCreateRequestDto dto = new MenuCreateRequestDto();
    dto.setDescription("TestDto");
    dto.setEnabled(true);
    dto.setTenantId(randomUUID().toString());
    when(menuRepository.save(any(Menu.class))).thenReturn(new Menu());
    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) + "/v1/menu", dto, ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testCanNotAddCategoryIfMenuNotPresent() {
    // Given
    when(menuRepository.save(any(Menu.class))).thenReturn(new Menu());
    when(menuRepository.findById(any(String.class))).thenReturn(Optional.of(new Menu()));

    CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
    dto.setDescription("test Category Description");
    dto.setName("test Category Name");

    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) + "/v1/menu/" + randomUUID() + "/category", dto, ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testInvalidRequestObjectGivenReturnsBadRequest() {
    // Given
    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) + "/v1/menu/"
                    + randomUUID() + "/category", new CreateCategoryRequestDto(), ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  void testInvalidMenuIdWilThrowBadRequest() {
    // Given

    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) +
                    "/v1/menu/something/category", new CreateCategoryRequestDto(), ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(BAD_REQUEST);
  }

  @Test
  void testAddCategory() {
    // Given
    when(menuRepository.save(any(Menu.class))).thenReturn(menu);
    when(menuRepository.findById(any(String.class))).thenReturn(Optional.of(menu));

    CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
    dto.setDescription("test Category Description");
    dto.setName("test Category Name");

    Menu initialSave = menuRepository.save(menu);
    // When
    var response =
            this.testRestTemplate.postForEntity(getBaseURL(port) + "/v1/menu/" + initialSave.getId() + "/category", dto, ResourceCreatedResponseDto.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> byId = menuRepository.findById(initialSave.getId());
    Menu updated = byId.get();
    then(updated.getId()).isEqualTo(initialSave.getId());
    then(updated.getCategories()).hasSize(1);
    Category savedCategory = updated.getCategories().get(0);
    then(savedCategory.getId()).isNotEmpty();
    then(savedCategory.getDescription()).isEqualTo(dto.getDescription());
    then(savedCategory.getName()).isEqualTo(dto.getName());
  }

}