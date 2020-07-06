package com.xxAMIDOxx.xxSTACKSxx.builder;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.List;

/**
 * Builder class for Menu
 * @author ArathyKrishna
 */
public final class MenuBuilder {
    private String id = null;
    private String name = null;
    private String description = null;
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

    public MenuBuilder withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public MenuBuilder withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Menu build() {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setDescription(description);
        menu.setCategories(categories);
        menu.setEnabled(enabled);
        return menu;
    }
}
