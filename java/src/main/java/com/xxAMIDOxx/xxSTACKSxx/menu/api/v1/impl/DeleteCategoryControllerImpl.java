package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteCategoryControllerImpl implements DeleteCategoryController {

  private MenuQueryService menuQueryService;

  public DeleteCategoryControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.DELETE_CATEGORY.getCode(),
                    correlationId));

    Category category = getCategory(menu, categoryId, correlationId);
    List<Category> collect =
            menu.getCategories().stream()
                    .filter(t -> !Objects.equals(t, category))
                    .collect(Collectors.toList());

    menu.setCategories(!collect.isEmpty() ? collect : Collections.emptyList());

    menuQueryService.update(menu);

    return new ResponseEntity<>(OK);
  }

  Category getCategory(Menu menu, UUID categoryId, String correlationId) {
    return findCategory(menu, categoryId)
            .orElseThrow(() -> new CategoryDoesNotExistException(categoryId.toString(), menu.getId(), OperationCode.DELETE_CATEGORY.getCode(), correlationId));
  }

  /**
   * Find a category for the id provided.
   *
   * @param menu menu object
   * @param categoryId category id
   * @return category if found else optional.empty
   */
  private Optional<Category> findCategory(Menu menu, UUID categoryId) {
    Optional<Category> existing = Optional.empty();
    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing = menu.getCategories().stream()
              .filter(c -> c.getId().equals(categoryId.toString()))
              .findFirst();
    }
    return existing;
  }
}
