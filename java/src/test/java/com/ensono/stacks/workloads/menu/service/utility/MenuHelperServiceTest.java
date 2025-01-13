package com.ensono.stacks.workloads.menu.service.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ensono.stacks.workloads.menu.domain.Category;
import com.ensono.stacks.workloads.menu.domain.Item;
import com.ensono.stacks.workloads.menu.domain.Menu;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class MenuHelperServiceTest {

  @Test
  void shouldAddOrUpdateCategoryIfMenuCategoriesNull() {

    Menu menu =
        new Menu(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            "menu name",
            "cat desc",
            null,
            true);

    MenuHelperService menuHelperService = new MenuHelperService();
    menuHelperService.addOrUpdateCategory(
        menu, new Category(UUID.randomUUID().toString(), "cat name", "cat desc", null));

    assertNotNull(menu.getCategories());
    assertEquals(1, menu.getCategories().size());
  }

  @Test
  void shouldAddOrUpdateItemIfCategoryItemsNull() {

    Category category = new Category(UUID.randomUUID().toString(), "cat name", "cat desc", null);

    Item item = new Item(UUID.randomUUID().toString(), "item name", "item desc", 100.0, true);

    MenuHelperService menuHelperService = new MenuHelperService();
    menuHelperService.addOrUpdateItem(category, item);

    assertNotNull(category.getItems());
    assertEquals(1, category.getItems().size());
  }
}
