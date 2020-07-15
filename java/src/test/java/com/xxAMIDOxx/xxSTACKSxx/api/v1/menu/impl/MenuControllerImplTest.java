package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.createMenus;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Integration")
public class MenuControllerImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuService service;

    @MockBean
    private MenuRepository menuRepository;

    @Test
    public void whenCalledForMenuReturnsOK() {
        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(port) + "/v1/menu/", SearchMenuResult.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnMenusWhenSearchedByRestaurantId() {
        // Given
        final int PAGE_NUMBER = 1;
        final int PAGE_SIZE = 20;
        final UUID RESTAURANT_ID = randomUUID();

        List<Menu> menuList = createMenus(3);
        List<SearchMenuResultItem> expectedMenuList = menuList.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList());

        SearchMenuResult expectedMenu = new SearchMenuResult(PAGE_SIZE, PAGE_NUMBER, expectedMenuList);

        when(service.findAll(PAGE_NUMBER, PAGE_SIZE)).thenReturn(menuList);

        // When
        var result = this.testRestTemplate.getForEntity(
                String.format("%s/v1/menu?restaurantId=%s", getBaseURL(port), RESTAURANT_ID),
                SearchMenuResult.class);
        // Then
        then(result.getBody().getPageNumber().equals(expectedMenu.getPageNumber()));
        then(result.getBody().getPageSize().equals(expectedMenu.getPageSize()));
        then(result.getBody().getResults().containsAll(expectedMenu.getResults()));
    }

    @Test
    public void returnMenuWhenSearchById() {
        // Given
        Menu menu = createMenu(0);
        when(service.findById(UUID.fromString(menu.getId())))
                .thenReturn(Optional.of(menu));

        // When
        var response = this.testRestTemplate.getForEntity(
                getBaseURL(port) + "/v1/menu/" + menu.getId(),
                Menu.class);

        // Then
        then(response.getBody()).isEqualTo(menu);
    }

    @Test
    public void testWhenPageSizeAndNoGetsDefaultedWhenNoValueGiven() {
        // Given
        Menu menu = createMenu(1);
        // When
        var response =
                this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu", SearchMenuResult.class);

        // Then
        then(response.getBody()).isInstanceOf(SearchMenuResult.class);
        SearchMenuResult actual = response.getBody();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(1));
        assertThat(actual.getPageSize(), is(20));
    }

    @Test
    public void testWhenPageSizeGivenReturnsResultsMatchingPageSize() {
        // Given
        Menu menu = createMenu(30);

        // When
        var response =
                this.testRestTemplate.getForEntity(
                        String.format("%s/v1/menu?pageSize=%s", getBaseURL(port), 10), SearchMenuResult.class);

        // Then
        then(response.getBody()).isInstanceOf(SearchMenuResult.class);
        SearchMenuResult actual = response.getBody();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(1));
        assertThat(actual.getPageSize(), is(10));
    }

    @Test
    public void testWhenNoSearchTermGivenReturnsAllResults() {
        // Given
        Menu menu = createMenu(30);
        // When
        var response =
                this.testRestTemplate.getForEntity(
                        String.format("%s/v1/menu?pageSize=%s", getBaseURL(port), 10), SearchMenuResult.class);

        // Then
        then(response.getBody()).isInstanceOf(SearchMenuResult.class);
        SearchMenuResult actual = response.getBody();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(1));
        assertThat(actual.getPageSize(), is(10));
        assertThat(actual.getResults(), is(notNullValue()));
    }

    @Test
    public void testWhenNoRestaurantIdGivenReturnsAllResults() {
        // Given
        Menu menu = createMenu(30);
        // When
        var response =
                this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu", SearchMenuResult.class);

        // Then
        then(response.getBody()).isInstanceOf(SearchMenuResult.class);
        SearchMenuResult actual = response.getBody();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(1));
        assertThat(actual.getPageSize(), is(20));
        assertThat(actual.getResults(), is(notNullValue()));
    }

    @Test
    public void testWhenSearchTermGivenReturnResultsMatchingTerm() {
        // Given
        List<Menu> menus = createMenus(20);

        // When
        var result = this.testRestTemplate.getForEntity(
                String.format("%s/v1/menu?searchTerm=%s", getBaseURL(port), "menu"),
                SearchMenuResult.class);

        // Then
        then(result.getBody()).isInstanceOf(SearchMenuResult.class);
    }
}
