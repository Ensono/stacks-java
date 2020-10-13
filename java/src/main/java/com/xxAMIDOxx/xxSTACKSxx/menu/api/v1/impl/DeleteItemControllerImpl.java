package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteItemControllerImpl implements DeleteItemController {

  private MenuQueryService menuQueryService;

  public DeleteItemControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<Void> deleteItem(
      UUID menuId, UUID categoryId, UUID itemId, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.UPDATE_CATEGORY.getCode(),
                    correlationId));

    Category category = getCategory(menu, categoryId, correlationId);
    Item item = getItem(category, itemId, menuId, correlationId);

    List<Item> itemList =
            category.getItems().stream()
                    .filter(t -> !Objects.equals(t, item))
                    .collect(Collectors.toList());
    category.setItems(!itemList.isEmpty() ? itemList : Collections.emptyList());

    menuQueryService.update(menu.addOrUpdateCategory(category));

    return new ResponseEntity<>(OK);
  }

  Category getCategory(Menu menu, UUID categoryId, String correlationId) {
    return findCategory(menu, categoryId)
            .orElseThrow(() -> new CategoryDoesNotExistException(categoryId.toString(), menu.getId(), OperationCode.UPDATE_MENU_ITEM.getCode(), correlationId));
  }

  Item getItem(Category category, UUID itemId, UUID menuId, String correlationId) {
    return findItem(category, itemId)
            .orElseThrow(() -> new ItemDoesNotExistsException(UUID.fromString(category.getId()), itemId, menuId, OperationCode.UPDATE_MENU_ITEM.getCode(), correlationId));
  }

  /**
   * Find a category for the id provided.
   *
   * @param menu menu object
   * @param categoryId category id
   * @return category if found else optional.empty
   */
  public Optional<Category> findCategory(Menu menu, UUID categoryId) {
    Optional<Category> existing = Optional.empty();
    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
              menu.getCategories().stream()
                      .filter(c -> c.getId().equals(categoryId.toString()))
                      .findFirst();
    }
    return existing;
  }

  public Optional<Item> findItem(Category category, UUID itemId) {
    Optional<Item> existing = Optional.empty();
    if (category.getItems() != null && !category.getItems().isEmpty()) {
      existing =
              category.getItems().stream().filter(t -> t.getId().equals(itemId.toString())).findFirst();
    }
    return existing;
  }
}
