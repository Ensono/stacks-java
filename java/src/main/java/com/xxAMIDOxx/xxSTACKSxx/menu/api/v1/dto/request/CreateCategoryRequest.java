package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CreateCategoryRequest {

    @JsonProperty("name")
    @NotBlank
    private String name = null;

    @JsonProperty("description")
    @NotBlank
    private String description = null;
}
