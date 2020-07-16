package com.xxAMIDOxx.xxSTACKSxx.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {

  private String id;

  private String name;

  private String description;

  private List<Item> items;

  public Category(String id, String name, String description, List<Item> items) {
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

  public List<Item> getItems() {
    return items;
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
