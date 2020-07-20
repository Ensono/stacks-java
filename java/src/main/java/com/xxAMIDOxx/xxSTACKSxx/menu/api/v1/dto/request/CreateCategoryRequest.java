package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class CreateCategoryRequest {

  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  @JsonProperty("description")
  @NotEmpty
  private String description = null;

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

}
