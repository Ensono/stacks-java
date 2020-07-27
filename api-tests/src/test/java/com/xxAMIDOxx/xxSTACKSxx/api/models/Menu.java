package com.xxAMIDOxx.xxSTACKSxx.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class Menu {

    @JsonProperty("id")
    private String id;

    @JsonProperty("restaurantId")
    private UUID restaurantId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("categories")
    private List<Category> categories;

    @JsonProperty("enabled")
    private Boolean enabled;

    public Menu() {
    }

    public Menu(String id, UUID restaurantId, String name, String description, List<Category> categories, Boolean enabled) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Boolean getEnabled() {
        return enabled;
    }
}
