package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.UpdateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
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
public class UpdateItemControllerImpl implements UpdateItemController {

  private MenuQueryService menuQueryService;
  private CategoryService categoryService;
  private ItemService itemService;

  public UpdateItemControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService, ItemService itemService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.itemService = itemService;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateItem(
      UUID menuId,
      UUID categoryId,
      UUID itemId,
      @Valid UpdateItemRequest body,
      String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.UPDATE_MENU_ITEM.getCode(), correlationId);

    Category category =
        categoryService.getCategory(
            menu, categoryId, correlationId, OperationCode.UPDATE_MENU_ITEM.getCode());

    Item updated =
        itemService.updateExistingItem(
            body,
            category,
            menuId,
            itemId,
            correlationId,
            OperationCode.UPDATE_MENU_ITEM.getCode());
    menu.addOrUpdateCategory(category.addOrUpdateItem(updated));

    menuQueryService.update(menu);

    return new ResponseEntity<>(new ResourceUpdatedResponse(itemId), HttpStatus.OK);
  }
}
