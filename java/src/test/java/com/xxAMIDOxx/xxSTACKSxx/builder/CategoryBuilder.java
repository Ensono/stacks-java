package com.xxAMIDOxx.xxSTACKSxx.builder;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for Category
 * @author ArathyKrishna
 */
public final class CategoryBuilder {
    private String id = null;
    private String name = null;
    private String description = null;
    private List<Item> items = new ArrayList<>();

    private CategoryBuilder() {
    }

    public static CategoryBuilder aCategory() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder withItems(List<Item> items) {
        this.items = items;
        return this;
    }

    public Category build() {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setItems(items);
        return category;
    }
}
