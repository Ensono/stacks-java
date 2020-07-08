package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
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

        when(service.all(PAGE_NUMBER, PAGE_SIZE)).thenReturn(menuList);

        // When
        var result = this.testRestTemplate.getForEntity(
                String.format("%s/v1/menu?restaurantId=%s", getBaseURL(port), RESTAURANT_ID),
                SearchMenuResult.class);
        // Then
        then(result.getBody()).isEqualTo(expectedMenu);
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

    private List<Menu> createMenus(int count) {
        List<Menu> menuList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            menuList.add(createMenu(i));
        }
        return menuList;
    }

    private Menu createMenu(int counter) {
        Menu item = new Menu();
        item.setDescription("Some item " + counter);
        item.setEnabled(true);
        item.setName("some Name " + counter);
        item.setId(randomUUID().toString());
        return item;
    }
}
