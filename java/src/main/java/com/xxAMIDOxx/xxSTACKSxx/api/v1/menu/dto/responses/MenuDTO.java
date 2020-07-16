package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.Document;
import com.microsoft.azure.spring.data.cosmosdb.core.mapping.PartitionKey;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author James Peet
 */
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

  public MenuDTO(UUID id, UUID restaurantId, String name, String description, List<CategoryDTO> categories, Boolean enabled) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.name = name;
    this.description = description;
    this.categories = categories;
    this.enabled = enabled;
  }

  public UUID getId() {
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

  public List<CategoryDTO> getCategories() {
    return categories;
  }

  public Boolean getEnabled() {
    return enabled;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MenuDTO)) return false;
    MenuDTO menu = (MenuDTO) o;
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
