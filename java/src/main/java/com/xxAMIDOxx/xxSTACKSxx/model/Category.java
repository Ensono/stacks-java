package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Category {

  @JsonProperty("Id")
  private String id = null;

  @JsonProperty("Name")
  private String name = null;

  @JsonProperty("Description")
  private String description = null;

  @JsonProperty("Items")
  @Valid
  private List<Item> items = new ArrayList<>();

  public Category() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;
    Category category = (Category) o;
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
