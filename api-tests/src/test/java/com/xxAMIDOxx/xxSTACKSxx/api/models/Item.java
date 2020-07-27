package com.xxAMIDOxx.xxSTACKSxx.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Price")
    private Double price;

    @JsonProperty("Available")
    private Boolean available;

    public Item() {
    }

    public Item(String id, String name, String description, Double price, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }
}
