package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateItemControllerImpl implements CreateItemController {

  private MenuQueryService menuQueryService;

  private UUID itemId;

  public CreateItemControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuItem(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.CREATE_MENU_ITEM.getCode(),
                    correlationId));

    itemId = UUID.randomUUID();
    Category category = addItem(getCategory(menu, categoryId, correlationId), body, menu.getId(), correlationId);

    menuQueryService.update(menu.addOrUpdateCategory(category));

    return new ResponseEntity<>(
        new ResourceCreatedResponse(itemId),
        HttpStatus.CREATED);
  }

  Category getCategory(Menu menu, UUID categoryId, String correlationId) {
    Optional<Category> existing = Optional.empty();

    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
              menu.getCategories().stream()
                      .filter(c -> c.getId().equals(categoryId.toString()))
                      .findFirst();
    }
    return existing.orElseThrow(
            () -> new CategoryDoesNotExistException(categoryId.toString(), menu.getId(), OperationCode.CREATE_MENU_ITEM.getCode(), correlationId));
  }

  Category addItem(Category category, CreateItemRequest request, String menuId, String correlationId) {

    itemId = UUID.randomUUID();
    List<Item> items = category.getItems() == null ? new ArrayList<>() : category.getItems();

    if (items.stream().anyMatch(c -> c.getName().equalsIgnoreCase(request.getName()))) {
      throw new ItemAlreadyExistsException(request.getName(), category.getId(), menuId, OperationCode.CREATE_MENU_ITEM.getCode(), correlationId);
    } else {
      Item item =
              new Item(
                      itemId.toString(),
                      request.getName(),
                      request.getDescription(),
                      request.getPrice(),
                      request.getAvailable());
      category.getItems().add(item);
      return category;
    }
  }
}
