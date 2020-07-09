package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.model;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.model.matcher.TypeMatchers;
import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.CategoryBuilder.aCategory;
import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.anItem;
import static com.xxAMIDOxx.xxSTACKSxx.builder.MenuBuilder.aMenu;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
public class MenuTest {

    @MockBean
    private MenuRepository menuRepository;

    @Test
    public void testMenusBuilder() {
        // Given
        Item item = anItem().withAvailable(true).withDescription(
                "Some Description").withName("1st Item").withPrice(12.34d)
                            .withId(UUID.randomUUID().toString()).build();
        Category category = aCategory().withDescription(
                "1st Category Description").withName("1st Category").withItems(
                List.of(item)).withId(UUID.randomUUID().toString()).build();
        Menu menu = aMenu().withCategories(List.of(category)).withDescription(
                "1st Menu Description").withEnabled(true).withName("1st Menu")
                           .withId(UUID.randomUUID().toString()).build();
        // When // Then
        assertThat(item, is(notNullValue()));
        assertThat(category, is(notNullValue()));
        assertThat(menu, TypeMatchers.matchesMenu(menu));
    }
}
