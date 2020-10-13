package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.UpdateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for updating category.
 *
 */
@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  private MenuQueryService menuQueryService;

  private UUID categoryId;

  public UpdateCategoryControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.UPDATE_CATEGORY.getCode(),
                    correlationId));

    menu.addOrUpdateCategory(updateCategory(menu, categoryId, body, correlationId));
    menuQueryService.update(menu);

    return new ResponseEntity<>(
        new ResourceUpdatedResponse(categoryId), OK);
  }

  /**
   * If the request is to update the name and description of a category, if there is a category with
   * the same name but only updating the description, then allow that. Else, throw a category already
   * exists exception. If a category with the same name doesn't exits then update the requested
   * category.
   *
   * @param menu menu
   * @param request update category request
   * @return category
   */
  private Category updateCategory(Menu menu, UUID categoryId, UpdateCategoryRequest request, String correlationId) {
    Category category = getCategory(menu, categoryId, correlationId);
    menu.getCategories()
            .forEach(t -> {
              if (t.getName().equalsIgnoreCase(request.getName())) {
                if (t.getId().equalsIgnoreCase(categoryId.toString())) {
                  category.setDescription(request.getDescription());
                } else {
                  throw new CategoryAlreadyExistsException(
                          request.getName(), menu.getId(), OperationCode.UPDATE_CATEGORY.getCode(), correlationId);
                }
              } else {
                category.setDescription(request.getDescription());
                category.setName(request.getName());
              }
            });

    return category;
  }

  Category getCategory(Menu menu, UUID categoryId, String correlationId) {
    return findCategory(menu, categoryId)
            .orElseThrow(() -> new CategoryDoesNotExistException(categoryId.toString(), menu.getId(), OperationCode.UPDATE_CATEGORY.getCode(), correlationId));
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
