package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.utilities.HelperMethods.getBaseURL;
import static com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.utilities.HelperMethods.toJson;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuControllerImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenCalledForMenuReturnsOK() {
        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(port) + "/v1/menu/", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnMenusWhenSearchedByRestaurantId()
    throws JsonProcessingException {
        // Given
        final int PAGE_NUMBER = 1;
        final int PAGE_SIZE = 20;
        UUID restaurantId = randomUUID();

        List<Menu> menuList = createMenus();

        SearchMenuResult expectedMenu = new SearchMenuResult();
        expectedMenu.setPageNumber(PAGE_NUMBER);
        expectedMenu.setPageSize(PAGE_SIZE);

        List<SearchMenuResultItem> expectedMenuList = menuList.stream().map(
                m -> new SearchMenuResultItem(UUID.fromString(m.getId()),
                                              restaurantId, m.getName(),
                                              m.getDescription(),
                                              m.getEnabled())).collect(
                Collectors.toList());
        expectedMenu.setResults(expectedMenuList);

        when(service.all(PAGE_NUMBER, PAGE_SIZE)).thenReturn(menuList);

        // When
        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(port) + "/v1/menu?restaurantId=" + restaurantId,
                Map.class);
        // Then
        SearchMenuResult actual = mapper.convertValue(entity.getBody(),
                                                      SearchMenuResult.class);
        then(toJson(mapper, actual)).isEqualTo(toJson(mapper, expectedMenu));
    }

    @Test
    public void returnMenuWhenSearchById() throws JsonProcessingException {
        // Given
        final int PAGE_NUMBER = 1;
        final int PAGE_SIZE = 20;
        UUID restaurantId = randomUUID();

        List<Menu> menuList = createMenus();

        SearchMenuResult expectedMenu = new SearchMenuResult();
        expectedMenu.setPageNumber(PAGE_NUMBER);
        expectedMenu.setPageSize(PAGE_SIZE);

        List<SearchMenuResultItem> expectedMenuList = menuList.stream().map(
                m -> new SearchMenuResultItem(UUID.fromString(m.getId()),
                                              restaurantId, m.getName(),
                                              m.getDescription(),
                                              m.getEnabled())).collect(
                Collectors.toList());
        expectedMenu.setResults(expectedMenuList);

        when(service.findById(expectedMenuList.get(0).getId())).thenReturn(
                menuList.get(0));

        // When
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                getBaseURL(port) + "/v1/menu/" + expectedMenuList.get(0)
                                                                 .getId(),
                Map.class);

        // Then
        Menu actual = mapper.convertValue(entity.getBody(), Menu.class);
        then(toJson(mapper, actual)).isEqualTo(toJson(mapper, menuList.get(0)));
    }

    private List<Menu> createMenus() {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Menu item = new Menu();
            item.setDescription("Some item " + i);
            item.setEnabled(true);
            item.setName("some Name " + i);
            item.setId(randomUUID().toString());
            menuList.add(item);
        }
        return menuList;
    }
}