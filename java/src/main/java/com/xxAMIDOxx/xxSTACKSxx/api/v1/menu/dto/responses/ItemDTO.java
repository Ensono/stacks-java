package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(name = "Item")
public class ItemDTO {

  @JsonProperty("id")
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("price")
  private Double price;

  @JsonProperty("available")
  private Boolean available;

  public ItemDTO(String id, String name, String description, Double price, Boolean available) {
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
    if (!(o instanceof ItemDTO)) return false;
    ItemDTO item = (ItemDTO) o;
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
