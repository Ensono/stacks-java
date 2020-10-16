package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  /**
   * Find an item or throw item not found exception.
   *
   * @param category category item is added to
   * @param itemId item id
   * @param menuId menu category is added to
   * @param correlationId correlation id
   * @return item
   */
  public Item getItem(
      Category category, UUID itemId, UUID menuId, String correlationId, int operationCode) {
    return findItem(category, itemId)
        .orElseThrow(
            () ->
                new ItemDoesNotExistsException(
                    UUID.fromString(category.getId()),
                    itemId,
                    menuId,
                    operationCode,
                    correlationId));
  }

  /**
   * Find an item using category and item id.
   *
   * @param category category item is added to
   * @param itemId item id
   * @return optional item
   */
  public Optional<Item> findItem(Category category, UUID itemId) {
    Optional<Item> existing = Optional.empty();
    if (category.getItems() != null && !category.getItems().isEmpty()) {
      existing =
          category.getItems().stream().filter(t -> t.getId().equals(itemId.toString())).findFirst();
    }
    return existing;
  }

  /**
   * If the request is to update the description/available/price of an existing item then allow
   * that. If the request is to update an item but an item with that name already exists, then throw
   * an exception. If there are no item with the same name then allow that.
   *
   * @param request update item request
   * @param category category
   * @param menuId menuId
   * @return item
   */
  public Item updateExistingItem(
      UpdateItemRequest request,
      Category category,
      UUID menuId,
      UUID itemId,
      String correlationId,
      int operationCode) {
    Item item = getItem(category, itemId, menuId, correlationId, operationCode);

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
                  throw new ItemAlreadyExistsException(
                      request.getName(), category.getId(), menuId, operationCode, correlationId);
                }
              } else {
                item.setAvailable(request.getAvailable());
                item.setDescription(request.getDescription());
                item.setName(request.getName());
                item.setPrice(request.getPrice());
              }
            });

    return item;
  }

  /**
   * Add an item on to the Category.
   *
   * @param category category the item to be added to
   * @param request create item request
   * @param menuId menu id the category linked to
   * @param correlationId correlation id
   * @param itemId item id
   * @return category
   */
  public Category addItem(
      Category category,
      CreateItemRequest request,
      UUID menuId,
      String correlationId,
      String itemId) {
    List<Item> items = category.getItems() == null ? new ArrayList<>() : category.getItems();

    if (items.stream().anyMatch(c -> c.getName().equalsIgnoreCase(request.getName()))) {
      throw new ItemAlreadyExistsException(
          request.getName(),
          category.getId(),
          menuId,
          OperationCode.CREATE_MENU_ITEM.getCode(),
          correlationId);
    } else {
      Item item =
          new Item(
              itemId,
              request.getName(),
              request.getDescription(),
              request.getPrice(),
              request.getAvailable());
      category.getItems().add(item);
      return category;
    }
  }

  /**
   * create Item created event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @param itemId itemId
   * @return list of Menu Event
   */
  public List<MenuEvent> createItemCreatedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryUpdatedEvent(operationCode, correlationId, menuId, categoryId),
        new MenuItemCreatedEvent(operationCode, correlationId, menuId, categoryId, itemId));
  }

  /**
   * create and publish Item updated event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @param itemId itemId
   * @return list of Menu Event
   */
  public List<MenuEvent> createItemUpdatedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryUpdatedEvent(operationCode, correlationId, menuId, categoryId),
        new MenuItemUpdatedEvent(operationCode, correlationId, menuId, categoryId, itemId));
  }

  /**
   * create and publish item deleted event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @param itemId itemId
   * @return list of Menu Event
   */
  public List<MenuEvent> createItemDeletedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryUpdatedEvent(operationCode, correlationId, menuId, categoryId),
        new MenuItemDeletedEvent(operationCode, correlationId, menuId, categoryId, itemId));
  }
}
