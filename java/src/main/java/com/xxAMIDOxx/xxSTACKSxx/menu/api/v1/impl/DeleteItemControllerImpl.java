package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.ItemService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteItemControllerImpl implements DeleteItemController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;
  private final ItemService itemService;
  private final ApplicationEventPublisher publisher;

  public DeleteItemControllerImpl(
      MenuQueryService menuQueryService,
      CategoryService categoryService,
      ItemService itemService,
      ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.itemService = itemService;
    this.publisher = publisher;
  }

  @Override
  public ResponseEntity<Void> deleteItem(
      UUID menuId, UUID categoryId, UUID itemId, String correlationId) {

    int operationCode = OperationCode.DELETE_MENU_ITEM.getCode();

    Menu menu = menuQueryService.findMenuOrThrowException(menuId, operationCode, correlationId);

    Category category = categoryService.getCategory(menu, categoryId, correlationId, operationCode);
    Item item = itemService.getItem(category, itemId, menuId, correlationId, operationCode);

    List<Item> itemList =
        category.getItems().stream()
            .filter(t -> !Objects.equals(t, item))
            .collect(Collectors.toList());
    category.setItems(!itemList.isEmpty() ? itemList : Collections.emptyList());

    menuQueryService.update(menu.addOrUpdateCategory(category));

    createAndPublishEvents(operationCode, correlationId, menuId, categoryId, itemId);

    return new ResponseEntity<>(OK);
  }

  /**
   * create and publish event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @param itemId itemId
   */
  public void createAndPublishEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    List<MenuEvent> eventList =
        Arrays.asList(
            new MenuUpdatedEvent(operationCode, correlationId, menuId),
            new CategoryUpdatedEvent(operationCode, correlationId, menuId, categoryId),
            new MenuItemDeletedEvent(operationCode, correlationId, menuId, categoryId, itemId));

    eventList.forEach(publisher::publish);
  }
}
