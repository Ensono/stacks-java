package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateItemRequest {

    @JsonProperty("name")
    @NotEmpty
    private String name = null;

    @JsonProperty("description")
    @NotEmpty
    private String description = null;

    @JsonProperty("price")
    @NotNull
    @Positive(message = "Price must be greater than zero")
    private Double price = null;

    @JsonProperty("available")
    @NotNull
    private Boolean available = null;

    public CreateItemRequest(@NotEmpty String name, @NotEmpty String description,
                             @NotNull Double price, @NotNull Boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getAvailable() {
        return available;
    }
}
