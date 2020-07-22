package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class CreateCategoryRequest {

  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  @JsonProperty("description")
  @NotEmpty
  private String description = null;
}
