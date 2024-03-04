package com.amido.stacks.workloads.menu.domain;

import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Menu {

  @Id private String id;

  private String restaurantId;

  private String name;

  private String description;

  @Builder.Default private List<Category> categories = new ArrayList<>();

  private Boolean enabled;
}
