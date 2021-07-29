package com.amido.stacks.menu.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Menu {

  @Id private String id;

  private String restaurantId;

  private String name;

  private String description;

  private List<Category> categories;

  private Boolean enabled;

  public Menu addOrUpdateCategory(Category category) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories =
        this.categories.stream()
            .filter(c -> !c.getId().equals(category.getId()))
            .collect(Collectors.toList());
    this.categories.add(category);
    return this;
  }
}
