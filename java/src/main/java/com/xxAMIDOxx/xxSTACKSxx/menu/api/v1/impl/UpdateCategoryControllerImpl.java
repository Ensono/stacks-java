package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.UpdateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Controller for updating category. */
@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;

  public UpdateCategoryControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {

    int operationCode = OperationCode.UPDATE_CATEGORY.getCode();

    Menu menu = menuQueryService.findMenuOrThrowException(menuId, operationCode, correlationId);

    menu.addOrUpdateCategory(
        categoryService.updateCategory(menu, categoryId, body, correlationId, operationCode));
    menuQueryService.update(menu);

    menuQueryService.publishEvents(
        categoryService.createCategoryUpdatedEvents(
            operationCode, correlationId, menuId, categoryId));

    return new ResponseEntity<>(new ResourceUpdatedResponse(categoryId), OK);
  }
}
