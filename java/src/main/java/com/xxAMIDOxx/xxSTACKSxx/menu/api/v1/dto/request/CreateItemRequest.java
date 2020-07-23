package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
