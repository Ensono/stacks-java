package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  private MenuQueryService menuQueryService;
  private CategoryService categoryService;

  public CreateCategoryControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.CREATE_CATEGORY.getCode(), correlationId);

    UUID categoryId = UUID.randomUUID();

    menu.setCategories(categoryService.addCategory(menu, body, correlationId, categoryId));

    menuQueryService.update(menu);

    return new ResponseEntity<>(new ResourceCreatedResponse(categoryId), HttpStatus.CREATED);
  }
}
