package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.UpdateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateItemControllerImpl implements UpdateItemController {

  private MenuQueryService menuQueryService;

  public UpdateItemControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateItem(
      UUID menuId,
      UUID categoryId,
      UUID itemId,
      @Valid UpdateItemRequest body,
      String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.UPDATE_MENU_ITEM.getCode(),
                    correlationId));

    Category category = getCategory(menu, categoryId, correlationId);

    Item updated = updateExistingItem(body, category, menuId, itemId, correlationId);
    menu.addOrUpdateCategory(category.addOrUpdateItem(updated));

    menuQueryService.update(menu);

    return new ResponseEntity<>(
        new ResourceUpdatedResponse(itemId), HttpStatus.OK);
  }

  /**
   * If the request is to update the description/available/price of an existing item then allow that.
   * If the request is to update an item but an item with that name already exists, then throw an
   * exception.
   * If there are no item with the same name then allow that.
   *
   * @param request update item request
   * @param category category
   * @param menuId menuId
   * @return item
   */
  Item updateExistingItem(UpdateItemRequest request, Category category, UUID menuId, UUID itemId, String correlationId) {
    Item item = getItem(category, itemId, menuId, correlationId);

    category
        .getItems()
        .forEach(
            t -> {
              if (t.getName().equalsIgnoreCase(request.getName())) {
                if (t.getId().equalsIgnoreCase(itemId.toString())) {
                  item.setAvailable(request.getAvailable());
                  item.setDescription(request.getDescription());
                  item.setPrice(request.getPrice());
                } else {
                  throw new ItemAlreadyExistsException(request.getName(), category.getId(), menuId, OperationCode.CREATE_MENU_ITEM.getCode(), correlationId);
                }
              } else {
                item.setAvailable(request.getAvailable());
                item.setDescription(request.getDescription());
                item.setName(request.getName());
                item.setPrice(request.getPrice());
              } });

    return item;
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
