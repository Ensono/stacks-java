package com.ensono.stacks.workloads.menu.service.utility;

import com.ensono.stacks.workloads.menu.domain.Category;
import com.ensono.stacks.workloads.menu.domain.Item;
import com.ensono.stacks.workloads.menu.domain.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MenuHelperService {

  public void addOrUpdateCategory(Menu menu, Category category) {

    if (menu.getCategories() == null) {
      menu.setCategories(new ArrayList<>());
    }

    List<Category> newCategories =
        menu.getCategories().stream()
            .filter(c -> !c.getId().equals(category.getId()))
            .collect(Collectors.toList());

    newCategories.add(category);

    menu.setCategories(newCategories);
  }

  public void addOrUpdateItem(Category category, Item item) {

    if (category.getItems() == null) {
      category.setItems(new ArrayList<>());
    }

    List<Item> newItems =
        category.getItems().stream()
            .filter(c -> !c.getId().equals(item.getId()))
            .collect(Collectors.toList());

    newItems.add(item);

    category.setItems(newItems);
  }
}
