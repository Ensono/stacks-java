package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.CreateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemCreatedEvent;
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
public class CreateItemControllerImpl implements CreateItemController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;
  private final ItemService itemService;
  private final ApplicationEventPublisher publisher;

  public CreateItemControllerImpl(
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
  public ResponseEntity<ResourceCreatedResponse> addMenuItem(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {

    int operationCode = OperationCode.CREATE_MENU_ITEM.getCode();

    Menu menu = menuQueryService.findMenuOrThrowException(menuId, operationCode, correlationId);

    UUID itemId = UUID.randomUUID();
    Category category =
        itemService.addItem(
            categoryService.getCategory(menu, categoryId, correlationId, operationCode),
            body,
            menuId,
            correlationId,
            itemId.toString());

    menuQueryService.update(menu.addOrUpdateCategory(category));

    createAndPublishEvents(operationCode, correlationId, menuId, categoryId, itemId);

    return new ResponseEntity<>(new ResourceCreatedResponse(itemId), HttpStatus.CREATED);
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
            new MenuItemCreatedEvent(operationCode, correlationId, menuId, categoryId, itemId));

    eventList.forEach(publisher::publish);
  }
}
