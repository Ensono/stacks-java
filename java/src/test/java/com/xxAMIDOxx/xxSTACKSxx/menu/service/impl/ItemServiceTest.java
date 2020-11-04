package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.ItemHelper.createItem;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.ItemHelper.createItems;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class ItemServiceTest {

  @Test
  void testGetItems() {
    // Given
    Menu menu = createMenu(1);
    Item item = createItem(1);
    Category category = createCategory(1);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);

    ItemService service = new ItemService();

    // When
    Item actual =
        service.getItem(category, fromString(item.getId()), fromString(menu.getId()), "", 0);

    // Then
    then(actual.getId()).isEqualTo(item.getId());
  }

  @Test
  void testGetItemsWithInvalidId() {
    // Given
    Menu menu = createMenu(1);
    Item item = createItem(1);
    Category category = createCategory(1);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);

    ItemService service = new ItemService();

    // When  // Then
    ItemDoesNotExistsException itemDoesNotExistsException =
        assertThrows(
            ItemDoesNotExistsException.class,
            () -> service.getItem(category, randomUUID(), fromString(menu.getId()), "", 0));
    then(itemDoesNotExistsException.getMessage()).contains("An item with the id");
  }

  @Test
  void testFindItem() {
    // Given
    Item item = createItem(1);
    Category category = createCategory(1);
    category.addOrUpdateItem(item);

    ItemService service = new ItemService();

    // When
    Optional<Item> optionalItem = service.findItem(category, fromString(item.getId()));

    // Then
    then(optionalItem.isEmpty()).isFalse();
    then(optionalItem.get().getId()).isEqualTo(item.getId());
  }

  @Test
  void testFindItemReturnsEmptyIfNoItemPresentInCategory() {
    // Given
    Category category = createCategory(1);
    ItemService service = new ItemService();

    // When
    Optional<Item> optionalItem = service.findItem(category, randomUUID());

    // Then
    then(optionalItem.isEmpty()).isTrue();
  }

  @Test
  void testUpdateExistingItem() {
    // Given
    UpdateItemRequest request =
        new UpdateItemRequest("Update Item", "Updating Description", 12d, false);
    Category category = createCategory(1);
    Item item = createItem(1);
    category.addOrUpdateItem(item);
    Menu menu = createMenu(1);
    menu.addOrUpdateCategory(category);

    ItemService service = new ItemService();

    // When
    Item updated =
        service.updateExistingItem(
            request, category, fromString(menu.getId()), fromString(item.getId()), "", 0);

    // Then
    then(updated.getId()).isEqualTo(item.getId());
    then(updated.getName()).isEqualTo(request.getName());
    then(updated.getDescription()).isEqualTo(request.getDescription());
    then(updated.getPrice()).isEqualTo(request.getPrice());
    then(updated.getAvailable()).isFalse();
  }

  @Test
  void testUpdateItemWithAnExistingItemNameWillThrowError() {
    // Given
    Category category = createCategory(1);
    List<Item> items = createItems(2);
    category.setItems(items);
    Menu menu = createMenu(1);
    menu.addOrUpdateCategory(category);
    UpdateItemRequest request =
        new UpdateItemRequest(items.get(0).getName(), "Updating Description", 12d, false);

    ItemService service = new ItemService();

    // When // Then
    ItemAlreadyExistsException itemAlreadyExistsException =
        assertThrows(
            ItemAlreadyExistsException.class,
            () ->
                service.updateExistingItem(
                    request,
                    category,
                    fromString(menu.getId()),
                    fromString(items.get(1).getId()),
                    "",
                    0));
    then(itemAlreadyExistsException.getMessage()).contains("An item with the name ");
  }

  @Test
  void testAddItem() {
    // Given
    Category category = createCategory(1);
    Menu menu = createMenu(1);
    menu.addOrUpdateCategory(category);
    CreateItemRequest request = new CreateItemRequest("New Item", "New Description", 15d, true);

    // When
    ItemService service = new ItemService();
    Category updated =
        service.addItem(category, request, fromString(menu.getId()), "", randomUUID().toString());

    // Then
    then(updated.getItems()).isNotEmpty();
    Item item = updated.getItems().get(0);
    then(item.getName()).isEqualTo(request.getName());
    then(item.getDescription()).isEqualTo(request.getDescription());
    then(item.getPrice()).isEqualTo(request.getPrice());
    then(item.getAvailable()).isTrue();
  }

  @Test
  void testEventsForItemCreation() {
    // Given an item creation request is received

    // When the item is created
    ItemService service = new ItemService();
    List<MenuEvent> itemCreatedEvents =
        service.itemCreatedEvents(
            OperationCode.CREATE_MENU_ITEM.getCode(), "", randomUUID(), randomUUID(), randomUUID());
    // Then
    then(itemCreatedEvents).hasSize(3);
  }

  @Test
  void testEventsForItemUpdate() {
    // Given an item update request is received

    // When the item is updated
    ItemService service = new ItemService();
    List<MenuEvent> itemCreatedEvents =
        service.itemUpdatedEvents(
            OperationCode.UPDATE_MENU_ITEM.getCode(), "", randomUUID(), randomUUID(), randomUUID());
    // Then
    then(itemCreatedEvents).hasSize(3);
  }

  @Test
  void testEventsForItemDeletion() {
    // Given an item deletion request is received

    // When the item is deleted
    ItemService service = new ItemService();
    List<MenuEvent> itemCreatedEvents =
        service.itemDeletedEvents(
            OperationCode.DELETE_MENU_ITEM.getCode(), "", randomUUID(), randomUUID(), randomUUID());
    // Then
    then(itemCreatedEvents).hasSize(3);
  }
}
