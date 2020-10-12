package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  private MenuQueryService menuQueryService;

  private UUID categoryId;

  public CreateCategoryControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.CREATE_CATEGORY.getCode(),
                    correlationId));

    menu.setCategories(addCategory(menu, body.getName(), body.getDescription(), correlationId));

    menuQueryService.update(menu);

    return new ResponseEntity<>(
        new ResourceCreatedResponse(categoryId),
        HttpStatus.CREATED);
  }

  private List<Category> addCategory(Menu menu, String categoryName, String categoryDescription, String correlationId) {
    categoryId = UUID.randomUUID();

    List<Category> categories =
            menu.getCategories() == null ? new ArrayList<>() : menu.getCategories();

    if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(categoryName))) {
      throw new CategoryAlreadyExistsException(categoryName, menu.getId(), OperationCode.CREATE_CATEGORY.getCode(), correlationId);
    } else {
      categories.add(
              new Category(
                      categoryId.toString(),
                      categoryName,
                      categoryDescription,
                      new ArrayList<>()));
      return categories;
    }
  }
}
