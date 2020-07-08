package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.model;


import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.model.matcher.TypeMatchers;
import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.CategoryBuilder.aCategory;
import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.anItem;
import static com.xxAMIDOxx.xxSTACKSxx.builder.MenuBuilder.aMenu;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
public class MenuTest {

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
