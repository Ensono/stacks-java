package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.UpdateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.ItemService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateItemControllerImpl implements UpdateItemController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;
  private final ItemService itemService;
  private final ApplicationEventPublisher publisher;

  public UpdateItemControllerImpl(
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
  public ResponseEntity<ResourceUpdatedResponse> updateItem(
      UUID menuId,
      UUID categoryId,
      UUID itemId,
      @Valid UpdateItemRequest body,
      String correlationId) {

    int operationCode = OperationCode.UPDATE_MENU_ITEM.getCode();

    Menu menu = menuQueryService.findMenuOrThrowException(menuId, operationCode, correlationId);

    Category category = categoryService.getCategory(menu, categoryId, correlationId, operationCode);

    Item updated =
        itemService.updateExistingItem(
            body, category, menuId, itemId, correlationId, operationCode);
    menu.addOrUpdateCategory(category.addOrUpdateItem(updated));

    menuQueryService.update(menu);

    createAndPublishEvents(operationCode, correlationId, menuId, categoryId, itemId);

    return new ResponseEntity<>(new ResourceUpdatedResponse(itemId), HttpStatus.OK);
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
            new MenuItemUpdatedEvent(operationCode, correlationId, menuId, categoryId, itemId));

    eventList.forEach(publisher::publish);
  }
}
