package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;

import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.createMenus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


@SpringBootTest
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Service")
public class MenuServiceImplTest {

    @Mock
    private MenuRepository repository;

    @MockBean
    private MenuService service;

    @Test
    public void testService() {
        // Given
        List<Menu> menuList = createMenus(10);
        List<SearchMenuResultItem> expectedMenuList = menuList.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList());
        // When
        List<Menu> all = service.findAll(1, 5);

        // Then
        assertThat(all, hasSize(0));
    }
}