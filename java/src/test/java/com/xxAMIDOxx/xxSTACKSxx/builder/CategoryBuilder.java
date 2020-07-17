package com.xxAMIDOxx.xxSTACKSxx.builder;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.ItemDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for CategoryDTO
 * @author ArathyKrishna
 */
public final class CategoryBuilder {
    private String id = null;
    private String name = null;
    private String description = null;
    private List<ItemDTO> items = new ArrayList<>();

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

    public CategoryBuilder withItems(List<ItemDTO> items) {
        this.items = items;
        return this;
    }

    public CategoryDTO build() {
        return new CategoryDTO(id, name, description, items);
    }
}
