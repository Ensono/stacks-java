package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class SearchMenuResultItem {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("restaurantId")
  private UUID restaurantId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("enabled")
  private Boolean enabled;

  public UUID getId() {
    return id;
  }

  public UUID getRestaurantId() {
    return restaurantId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setRestaurantId(UUID restaurantId) {
    this.restaurantId = restaurantId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
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

  @Override
  public String toString() {
    return "SearchMenuResultItem{" +
            "id=" + id +
            ", restaurantId=" + restaurantId +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", enabled=" + enabled +
            '}';
  }
}
