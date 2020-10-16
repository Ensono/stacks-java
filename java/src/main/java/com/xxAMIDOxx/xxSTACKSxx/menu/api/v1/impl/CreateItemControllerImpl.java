package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.CreateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.ItemService;
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

  public CreateItemControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService, ItemService itemService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.itemService = itemService;
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

    menuQueryService.publishEvents(
        itemService.createItemCreatedEvents(
            operationCode, correlationId, menuId, categoryId, itemId));

    return new ResponseEntity<>(new ResourceCreatedResponse(itemId), HttpStatus.CREATED);
  }
}
