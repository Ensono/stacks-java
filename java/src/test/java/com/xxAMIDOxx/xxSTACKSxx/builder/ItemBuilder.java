package com.xxAMIDOxx.xxSTACKSxx.builder;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.ItemDTO;

import java.util.UUID;

/**
 * Builder class for CategoryDTO ItemDTO
 * @author ArathyKrishna
 */
public final class ItemBuilder {
    private String id = null;
    private String name = null;
    private String description = null;
    private Double price = null;
    private Boolean available = null;

    private ItemBuilder() {
    }

    public static ItemBuilder anItem() {
        return new ItemBuilder();
    }

    public ItemBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public ItemBuilder withAvailable(Boolean available) {
        this.available = available;
        return this;
    }

    public ItemDTO build() {
        return new ItemDTO(id, name, description, price, available);
    }

    public static ItemDTO aDefaultItem() {
        return anItem()
                .withAvailable(true)
                .withDescription("Some Description")
                .withName("1st ItemDTO")
                .withPrice(12.34d)
                .withId(UUID.randomUUID().toString())
                .build();
    }
}
