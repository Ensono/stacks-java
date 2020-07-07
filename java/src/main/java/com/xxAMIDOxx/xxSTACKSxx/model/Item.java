package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Item {

  @JsonProperty("Id")
  private UUID id = null;

  @JsonProperty("Name")
  private String name = null;

  @JsonProperty("Description")
  private String description = null;

  @JsonProperty("Price")
  private Double price = null;

  @JsonProperty("Available")
  private Boolean available = null;

  public Item() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
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

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
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
