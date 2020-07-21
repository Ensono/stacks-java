package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Document(collection = "Menu")
public class Menu {

  @Id
  @PartitionKey
  private String id;

  private String restaurantId;

  private String name;

  private String description;

  private List<Category> categories;

  private Boolean enabled;

  public Menu(String id, String restaurantId, String name, String description, List<Category> categories, Boolean enabled) {
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

  public String getRestaurantId() {
    return restaurantId;
  }

  public void setRestaurantId(String restaurantId) {
    this.restaurantId = restaurantId;
  }

  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public Menu addUpdateCategory(Category category) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories = this.categories
            .stream()
            .filter(c -> !c.getId().equals(category.getId()))
            .collect(Collectors.toList());
    this.categories.add(category);
    return this;
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
