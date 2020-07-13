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

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("TenantId")
  private UUID restaurantId;

  @JsonProperty("Categories")
  private List<Category> categories;

  @JsonProperty("Enabled")
  private Boolean enabled;

  public Menu() {
  }

  public Menu(String id, String name, String description, UUID restaurantId, List<Category> categories, Boolean enabled) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.restaurantId = restaurantId;
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
            Objects.equals(name, menu.name) &&
            Objects.equals(description, menu.description) &&
            Objects.equals(restaurantId, menu.restaurantId) &&
            Objects.equals(categories, menu.categories) &&
            Objects.equals(enabled, menu.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, restaurantId, categories, enabled);
  }
}
