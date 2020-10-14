package com.xxAMIDOxx.xxSTACKSxx.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {

  private String id;

  private String name;

  private String description;

  private List<Item> items;

  /**
   * Add or update item in a category
   *
   * @param item item to be added to category
   * @return category
   */
  public Category addOrUpdateItem(Item item) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items =
        this.items.stream()
            .filter(c -> !c.getId().equals(item.getId()))
            .collect(Collectors.toList());
    this.items.add(item);
    return this;
  }
}
