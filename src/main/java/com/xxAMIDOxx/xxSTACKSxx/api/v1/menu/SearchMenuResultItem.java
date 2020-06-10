package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchMenuResultItem {

  @JsonProperty("id")
  private UUID id = null;

  @JsonProperty("restaurantId")
  private UUID restaurantId = null;

  @JsonProperty("name")
  private String name = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("enabled")
  private Boolean enabled = null;
}
