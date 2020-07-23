package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class CreateItemRequest {

    @JsonProperty("name")
    @NotBlank
    private String name = null;

    @JsonProperty("description")
    @NotBlank
    private String description = null;

    @JsonProperty("price")
    @NotNull
    @Positive(message = "Price must be greater than zero")
    private Double price = null;

    @JsonProperty("available")
    @NotNull
    private Boolean available = null;
}
