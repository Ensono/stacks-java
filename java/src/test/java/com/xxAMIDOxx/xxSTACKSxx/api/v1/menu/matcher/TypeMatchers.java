package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.hamcrest.Matcher;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/**
 * Hamcrest Type Matchers for objects
 */
public
class TypeMatchers {

    public static
    Matcher<Item> matchesItem(Item expected) {
        return allOf(hasProperty("id", is(expected.getId())), hasProperty("name", is(expected.getName())),
                     hasProperty("description", is(expected.getDescription())),
                     hasProperty("price", is(expected.getPrice())),
                     hasProperty("available", is(expected.getAvailable())));
    }

    public static
    Matcher<Category> matchesCategory(Category expected) {
        return allOf(hasProperty("id", is(expected.getId())), hasProperty("name", is(expected.getName())),
                     hasProperty("description", is(expected.getDescription())), hasProperty("items", containsInAnyOrder(
                        expected.getItems().stream().map(TypeMatchers::matchesItem).collect(Collectors.toSet()))));
    }

    public static
    Matcher<Menu> matchesMenu(Menu expected) {
        return allOf(hasProperty("id", is(expected.getId())), hasProperty("name", is(expected.getName())),
                     hasProperty("description", is(expected.getDescription())),
                     hasProperty("enabled", is(expected.getEnabled())), hasProperty("categories", containsInAnyOrder(
                        expected.getCategories().stream().map(TypeMatchers::matchesCategory)
                                .collect(Collectors.toSet()))));
    }
}
