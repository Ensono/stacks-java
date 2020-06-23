package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

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

  public SearchMenuResultItem() {
  }

  public SearchMenuResultItem(UUID id, UUID restaurantId, String name, String description, Boolean enabled) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.name = name;
    this.description = description;
    this.enabled = enabled;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(UUID restaurantId) {
    this.restaurantId = restaurantId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
