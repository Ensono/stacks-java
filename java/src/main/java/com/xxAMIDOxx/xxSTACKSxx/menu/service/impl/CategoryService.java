package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  /**
   * Find a category for the id provided.
   *
   * @param menu menu object
   * @param categoryId category id
   * @return category if found else optional.empty
   */
  public Optional<Category> findCategory(Menu menu, UUID categoryId) {
    Optional<Category> existing = Optional.empty();
    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
          menu.getCategories().stream()
              .filter(c -> c.getId().equals(categoryId.toString()))
              .findFirst();
    }
    return existing;
  }

  /**
   * find a category using category id and menu if not throw category not found exception.
   *
   * @param menu menu
   * @param categoryId category id
   * @param correlationId correlation id
   * @param operationCode operation code
   * @return category
   */
  public Category getCategory(Menu menu, UUID categoryId, String correlationId, int operationCode) {
    return findCategory(menu, categoryId)
        .orElseThrow(
            () ->
                new CategoryDoesNotExistException(
                    categoryId.toString(), menu.getId(), operationCode, correlationId));
  }

  /**
   * If the request is to update the name and description of a category, if there is a category with
   * the same name but only updating the description, then allow that. Else, throw a category
   * already exists exception. If a category with the same name doesn't exits then update the
   * requested category.
   *
   * @param menu menu
   * @param request update category request
   * @return category
   */
  public Category updateCategory(
      Menu menu,
      UUID categoryId,
      UpdateCategoryRequest request,
      String correlationId,
      int operationCode) {
    Category category = getCategory(menu, categoryId, correlationId, operationCode);
    menu.getCategories()
        .forEach(
            t -> {
              if (t.getName().equalsIgnoreCase(request.getName())) {
                if (t.getId().equalsIgnoreCase(categoryId.toString())) {
                  category.setDescription(request.getDescription());
                } else {
                  throw new CategoryAlreadyExistsException(
                      request.getName(), menu.getId(), operationCode, correlationId);
                }
              } else {
                category.setDescription(request.getDescription());
                category.setName(request.getName());
              }
            });

    return category;
  }

  /**
   * Add a category.
   *
   * @param menu menu category to be added to
   * @param body create category request
   * @param correlationId correlation id
   * @param categoryId new category id
   * @return list of categories in the menu
   */
  public List<Category> addCategory(
      Menu menu, CreateCategoryRequest body, String correlationId, UUID categoryId) {

    List<Category> categories =
        menu.getCategories() == null ? new ArrayList<>() : menu.getCategories();

    if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(body.getName()))) {
      throw new CategoryAlreadyExistsException(
          body.getName(), menu.getId(), OperationCode.CREATE_CATEGORY.getCode(), correlationId);
    } else {
      categories.add(
          new Category(
              categoryId.toString(), body.getName(), body.getDescription(), new ArrayList<>()));
      return categories;
    }
  }

  /**
   * create Category created event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @return list of Menu Event
   */
  public List<MenuEvent> createCategoryCreatedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryCreatedEvent(operationCode, correlationId, menuId, categoryId));
  }

  /**
   * create category updated event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @return list of Menu Event
   */
  public List<MenuEvent> createCategoryUpdatedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryUpdatedEvent(operationCode, correlationId, menuId, categoryId));
  }

  /**
   * create category deleted event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   * @return list of Menu Event
   */
  public List<MenuEvent> createCategoryDeletedEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    return Arrays.asList(
        new MenuUpdatedEvent(operationCode, correlationId, menuId),
        new CategoryDeletedEvent(operationCode, correlationId, menuId, categoryId));
  }
}
