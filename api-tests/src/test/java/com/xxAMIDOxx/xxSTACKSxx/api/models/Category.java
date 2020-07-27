package com.xxAMIDOxx.xxSTACKSxx.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Category {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Items")
    private List<Item> items = new ArrayList<>();

    public Category() {
    }

    public Category(String id, String name, String description, List<Item> items) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Item> getItems() {
        return items;
    }

}
