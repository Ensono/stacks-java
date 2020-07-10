package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class Item {

  @JsonProperty("Id")
  private String id;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Description")
  private String description;

  @JsonProperty("Price")
  private Double price;

  @JsonProperty("Available")
  private Boolean available;

  public Item() {
  }

  public Item(String id, String name, String description, Double price, Boolean available) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.available = available;
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

  public Double getPrice() {
    return price;
  }

  public Boolean getAvailable() {
    return available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Item)) return false;
    Item item = (Item) o;
    return Objects.equals(id, item.id) &&
            Objects.equals(name, item.name) &&
            Objects.equals(description, item.description) &&
            Objects.equals(price, item.price) &&
            Objects.equals(available, item.available);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, price, available);
  }
}
