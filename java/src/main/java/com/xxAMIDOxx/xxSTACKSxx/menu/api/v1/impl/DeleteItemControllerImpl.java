package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.ItemService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteItemControllerImpl implements DeleteItemController {

  private MenuQueryService menuQueryService;
  private CategoryService categoryService;
  private ItemService itemService;

  public DeleteItemControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService, ItemService itemService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.itemService = itemService;
  }

  @Override
  public ResponseEntity<Void> deleteItem(
      UUID menuId, UUID categoryId, UUID itemId, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.DELETE_MENU_ITEM.getCode(), correlationId);

    Category category =
        categoryService.getCategory(
            menu, categoryId, correlationId, OperationCode.DELETE_MENU_ITEM.getCode());
    Item item =
        itemService.getItem(
            category, itemId, menuId, correlationId, OperationCode.DELETE_MENU_ITEM.getCode());

    List<Item> itemList =
        category.getItems().stream()
            .filter(t -> !Objects.equals(t, item))
            .collect(Collectors.toList());
    category.setItems(!itemList.isEmpty() ? itemList : Collections.emptyList());

    menuQueryService.update(menu.addOrUpdateCategory(category));

    return new ResponseEntity<>(OK);
  }
}
