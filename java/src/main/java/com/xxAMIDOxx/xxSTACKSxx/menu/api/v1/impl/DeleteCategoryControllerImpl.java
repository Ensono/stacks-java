package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteCategoryControllerImpl implements DeleteCategoryController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;

  public DeleteCategoryControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
  }

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId, String correlationId) {

    int operationCode = OperationCode.DELETE_CATEGORY.getCode();

    Menu menu = menuQueryService.findMenuOrThrowException(menuId, operationCode, correlationId);

    Category category = categoryService.getCategory(menu, categoryId, correlationId, operationCode);
    List<Category> collect =
        menu.getCategories().stream()
            .filter(t -> !Objects.equals(t, category))
            .collect(Collectors.toList());

    menu.setCategories(!collect.isEmpty() ? collect : Collections.emptyList());

    menuQueryService.update(menu);

    menuQueryService.publishEvents(
        categoryService.categoryDeletedEvents(operationCode, correlationId, menuId, categoryId));

    return new ResponseEntity<>(OK);
  }
}
