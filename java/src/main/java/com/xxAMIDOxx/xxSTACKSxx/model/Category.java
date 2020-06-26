package com.xxAMIDOxx.xxSTACKSxx.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Category {

  @JsonProperty("Id")
  private UUID id = null;

  @JsonProperty("Name")
  private String name = null;

  @JsonProperty("Description")
  private String description = null;

  @JsonProperty("Items")
  @Valid
  private List<Item> items = new ArrayList<>();

  public Category() {
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

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }
}
