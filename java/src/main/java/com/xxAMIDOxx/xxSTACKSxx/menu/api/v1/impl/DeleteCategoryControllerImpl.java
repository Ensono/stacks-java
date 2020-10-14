package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
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

  private MenuQueryService menuQueryService;
  private final CategoryService categoryService;

  public DeleteCategoryControllerImpl(
      MenuQueryService menuQueryService, CategoryService categoryService) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
  }

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.DELETE_CATEGORY.getCode(), correlationId);

    Category category =
        categoryService.getCategory(
            menu, categoryId, correlationId, OperationCode.DELETE_CATEGORY.getCode());
    List<Category> collect =
        menu.getCategories().stream()
            .filter(t -> !Objects.equals(t, category))
            .collect(Collectors.toList());

    menu.setCategories(!collect.isEmpty() ? collect : Collections.emptyList());

    menuQueryService.update(menu);

    return new ResponseEntity<>(OK);
  }
}