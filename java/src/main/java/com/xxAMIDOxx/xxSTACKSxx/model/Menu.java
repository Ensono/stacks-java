package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author James Peet
 */
@Document(collection = "Menu")
public class Menu {

  @Id
  @PartitionKey
  @JsonProperty("id")
  private String id;

  @JsonProperty("restaurantId")
  private UUID restaurantId;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("categories")
  private List<Category> categories;

  @JsonProperty("enabled")
  private Boolean enabled;

  public Menu() {
  }

  public Menu(String id, UUID restaurantId, String name, String description, List<Category> categories, Boolean enabled) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.name = name;
    this.description = description;
    this.categories = categories;
    this.enabled = enabled;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public UUID getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(UUID restaurantId) {
  this.restaurantId = restaurantId;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public Boolean getEnabled() {
    return enabled;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Menu)) return false;
    Menu menu = (Menu) o;
    return Objects.equals(id, menu.id) &&
            Objects.equals(restaurantId, menu.restaurantId) &&
            Objects.equals(name, menu.name) &&
            Objects.equals(description, menu.description) &&
            Objects.equals(categories, menu.categories) &&
            Objects.equals(enabled, menu.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, restaurantId, name, description, categories, enabled);
  }
}
