package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "Menu")
public class MenuDTO {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("restaurantId")
  private UUID restaurantId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("categories")
  private List<CategoryDTO> categories;

  @JsonProperty("enabled")
  private Boolean enabled;
}
