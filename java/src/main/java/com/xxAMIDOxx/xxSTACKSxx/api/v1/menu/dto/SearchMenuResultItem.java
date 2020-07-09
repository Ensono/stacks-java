package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.Objects;
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

  public SearchMenuResultItem(Menu menu) {
    this.id = UUID.fromString(menu.getId());
    this.restaurantId = null;
    this.name = menu.getName();
    this.description = menu.getDescription();
    this.enabled = menu.getEnabled();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SearchMenuResultItem)) return false;
    SearchMenuResultItem that = (SearchMenuResultItem) o;
    return Objects.equals(id, that.id) &&
            Objects.equals(restaurantId, that.restaurantId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(enabled, that.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, restaurantId, name, description, enabled);
  }
}
