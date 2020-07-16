package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.ItemDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.MenuDTO;
import org.hamcrest.Matcher;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

/**
 * Hamcrest Matchers for objects
 *
 * @author ArathyKrishna
 */
public class TypeMatchers {

  public static Matcher<ItemDTO> matchesItem(ItemDTO expected) {
    return allOf(
        hasProperty("id", is(expected.getId())),
        hasProperty("name", is(expected.getName())),
        hasProperty("description", is(expected.getDescription())),
        hasProperty("price", is(expected.getPrice())),
        hasProperty("available", is(expected.getAvailable())));
  }

  public static Matcher<CategoryDTO> matchesCategory(CategoryDTO expected) {
    return allOf(
        hasProperty("id", is(expected.getId())),
        hasProperty("name", is(expected.getName())),
        hasProperty("description", is(expected.getDescription())),
        hasProperty(
                "items",
                containsInAnyOrder(
                    expected.getItems().stream()
                        .map(TypeMatchers::matchesItem)
                        .collect(Collectors.toSet()))));
  }

  public static Matcher<MenuDTO> matchesMenu(MenuDTO expected) {
    return allOf(
        hasProperty("id", is(expected.getId())),
        hasProperty("name", is(expected.getName())),
        hasProperty("description", is(expected.getDescription())),
        hasProperty("enabled", is(expected.getEnabled())),
        hasProperty("restaurantId", is(expected.getRestaurantId())),
        hasProperty(
            "categories",
            containsInAnyOrder(
                expected.getCategories().stream()
                    .map(TypeMatchers::matchesCategory)
                    .collect(Collectors.toSet()))));
  }
}
