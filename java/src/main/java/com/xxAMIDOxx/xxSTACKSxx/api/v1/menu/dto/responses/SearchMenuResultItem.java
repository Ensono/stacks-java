package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;

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

  public SearchMenuResultItem() {
  }

  public SearchMenuResultItem(Menu menu) {
    this.id = UUID.fromString(menu.getId());
    this.restaurantId = UUID.fromString(menu.getRestaurantId());
    this.name = menu.getName();
    this.description = menu.getDescription();
    this.enabled = menu.getEnabled();
  }

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
