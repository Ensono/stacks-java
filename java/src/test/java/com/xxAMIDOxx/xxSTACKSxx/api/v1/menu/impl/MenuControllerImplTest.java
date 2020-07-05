package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class MenuControllerImplTest {

    @Value("${local.management.port}")
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuService service;

    private ObjectMapper mapper = new ObjectMapper();

    private String getBaseURL(final int port) {
        return "http://localhost:" + port;
    }

    @Test
    public void whenCalledForMenuReturnsOK() {
        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/v1/menu/", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void returnMenusWhenSearchedByRestaurantId() throws JsonProcessingException {
        // Given
        int pageNumber = 1;
        int pageSize = 20;
        UUID restaurantId = randomUUID();

        List<Menu> menuList = createMenus();

        SearchMenuResult expectedMenu = new SearchMenuResult();
        expectedMenu.setPageNumber(pageNumber);
        expectedMenu.setPageSize(pageSize);

        List<SearchMenuResultItem> expectedMenuList = menuList.stream()
                .map(m -> new SearchMenuResultItem(UUID.fromString(m.getId()),
                        restaurantId,
                        m.getName(),
                        m.getDescription(),
                        m.getEnabled())).collect(Collectors.toList());
        expectedMenu.setResults(expectedMenuList);

        // When
        when(service.all(pageNumber, pageSize)).thenReturn(menuList);

        // Then
        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/v1/menu?restaurantId=" + restaurantId, Map.class);

        SearchMenuResult actual = mapper.convertValue(entity.getBody(), SearchMenuResult.class);
        then(toJson(mapper, actual)).isEqualTo(toJson(mapper, expectedMenu));
    }

    @Test
    public void returnMenuWhenSearchById() throws JsonProcessingException {
        // Given
        int pageNumber = 1;
        int pageSize = 20;
        UUID restaurantId = randomUUID();

        List<Menu> menuList = createMenus();

        SearchMenuResult expectedMenu = new SearchMenuResult();
        expectedMenu.setPageNumber(pageNumber);
        expectedMenu.setPageSize(pageSize);

        List<SearchMenuResultItem> expectedMenuList = menuList.stream()
                .map(m -> new SearchMenuResultItem(UUID.fromString(m.getId()),
                        restaurantId,
                        m.getName(),
                        m.getDescription(),
                        m.getEnabled())).collect(Collectors.toList());
        expectedMenu.setResults(expectedMenuList);

        // When
        when(service.findById(expectedMenuList.get(0).getId())).thenReturn(menuList.get(0));

        // Then
        ResponseEntity<Map> entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/v1/menu/" + expectedMenuList.get(0).getId(), Map.class);

        Menu actual = mapper.convertValue(entity.getBody(), Menu.class);
        then(toJson(mapper, actual)).isEqualTo(toJson(mapper, menuList.get(0)));
    }

    private String toJson(ObjectMapper mapper, Object actual) throws JsonProcessingException {
        return mapper.writeValueAsString(actual);
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