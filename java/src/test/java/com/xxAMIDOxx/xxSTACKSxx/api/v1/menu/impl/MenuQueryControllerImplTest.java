package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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

import static com.xxAMIDOxx.xxSTACKSxx.model.MenuHelper.*;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.*;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Integration")
public class MenuQueryControllerImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuRepository menuRepository;

    final int DEFAULT_PAGE_NUMBER = 1;
    final int DEFAULT_PAGE_SIZE = 20;

    @Test
    public void listMenusAndPagination() {

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
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(pageNumber));
        assertThat(actual.getPageSize(), is(pageSize));
    }

    @Test
    public void listMenusFilteredByRestaurantId() {

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
        then(result.getBody().getPageNumber()).isEqualTo(expectedResponse.getPageNumber());
        then(result.getBody().getPageSize()).isEqualTo(expectedResponse.getPageSize());
        then(result.getBody().getResults()).containsAll(expectedResponse.getResults());
    }

    @Test
    public void listMenusFilteredByRestaurantIdAndSearchTerm() {
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
    public void listMenusFilteredBySearchTerm() {
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
    public void getMenuById() {
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
    public void listMenusWithDefaultPagination() {
        // Given
        when(
                menuRepository.findAll(any(Pageable.class))
        ).thenReturn(new PageImpl<>(createMenus(1)));

        // When
        var response = this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu",
                        SearchMenuResult.class);

        // Then
        then(response.getBody()).isInstanceOf(SearchMenuResult.class);
        SearchMenuResult actual = response.getBody();
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getPageNumber(), is(DEFAULT_PAGE_NUMBER));
        assertThat(actual.getPageSize(), is(DEFAULT_PAGE_SIZE));
    }

}
