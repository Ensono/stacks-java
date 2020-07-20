package com.xxAMIDOxx.xxSTACKSxx.builder;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;

import java.util.List;

/**
 * Builder class for MenuDTO
 * @author ArathyKrishna
 */
public final class MenuBuilder {
    private String id = null;
    private String name = null;
    private String description = null;
    private String restaurantId = null;
    private List<Category> categories = null;
    private Boolean enabled = null;

    private MenuBuilder() {
    }

    public static MenuBuilder aMenu() {
        return new MenuBuilder();
    }

    public MenuBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public MenuBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MenuBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MenuBuilder withRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
        return this;
    }
    public MenuBuilder withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public MenuBuilder withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Menu build() {
        return new Menu(id, restaurantId, name, description, categories, enabled);
    }
}
