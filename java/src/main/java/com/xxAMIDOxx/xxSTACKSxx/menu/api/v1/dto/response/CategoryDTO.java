package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Objects;

@Schema(name = "Category")
public class CategoryDTO {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("items")
  private List<ItemDTO> items;

  public CategoryDTO(String id, String name, String description, List<ItemDTO> items) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.items = items;
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

  public List<ItemDTO> getItems() {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CategoryDTO)) return false;
    CategoryDTO category = (CategoryDTO) o;
    return Objects.equals(id, category.id) &&
            Objects.equals(name, category.name) &&
            Objects.equals(description, category.description) &&
            Objects.equals(items, category.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, items);
  }
}
