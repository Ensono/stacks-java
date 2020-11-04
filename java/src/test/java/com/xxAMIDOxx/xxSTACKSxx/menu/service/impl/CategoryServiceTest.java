package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategories;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class CategoryServiceTest {

  @Test
  void testFindCategory() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(1);
    menu.addOrUpdateCategory(category);

    // When
    CategoryService service = new CategoryService();
    Optional<Category> actual = service.findCategory(menu, fromString(category.getId()));

    // Then
    then(actual.isEmpty()).isFalse();
    Category returned = actual.get();
    then(returned.getName()).isEqualTo(category.getName());
    then(returned.getId()).isEqualTo(category.getId());
    then(returned.getDescription()).isEqualTo(category.getDescription());
  }

  @Test
  void testFindCategoryWithInvalidId() {
    // Given
    Menu menu = createMenu(1);

    // When
    CategoryService service = new CategoryService();
    Optional<Category> actual = service.findCategory(menu, randomUUID());

    // Then
    then(actual.isEmpty()).isTrue();
  }

  @Test
  void testGetCategoryWithInvalidIdWillThrowException() {
    // Given
    Menu menu = createMenu(1);

    // When
    CategoryService service = new CategoryService();

    // Then
    CategoryDoesNotExistException categoryDoesNotExistException =
        assertThrows(
            CategoryDoesNotExistException.class,
            () -> service.getCategory(menu, randomUUID(), "", 1));

    then(categoryDoesNotExistException.getMessage()).contains("A category with the id ");
  }

  @Test
  void testUpdateCategory() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(1);
    menu.addOrUpdateCategory(category);
    UpdateCategoryRequest request =
        new UpdateCategoryRequest("Updated Category", "Updating Description");

    // When
    CategoryService service = new CategoryService();
    Category updated = service.updateCategory(menu, fromString(category.getId()), request, "", 1);

    // Then
    then(updated.getDescription()).isEqualTo(request.getDescription());
    then(updated.getId()).isEqualTo(category.getId());
    then(updated.getName()).isEqualTo(request.getName());
  }

  @Test
  void testUpdateCategoryWithInvalidIdThrowsException() {
    // Given
    Menu menu = createMenu(1);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);
    UpdateCategoryRequest request =
        new UpdateCategoryRequest(categories.get(0).getName(), "Updating Description");

    // When
    CategoryService service = new CategoryService();

    // Then
    CategoryAlreadyExistsException categoryAlreadyExistsException =
        assertThrows(
            CategoryAlreadyExistsException.class,
            () ->
                service.updateCategory(
                    menu, fromString(categories.get(1).getId()), request, "", 1));
    then(categoryAlreadyExistsException.getMessage()).contains("A category with the name ");
  }

  @Test
  void testAddCategory() {
    // Given
    Menu menu = createMenu(1);
    CreateCategoryRequest request = new CreateCategoryRequest("New category", "New description");

    // When
    CategoryService service = new CategoryService();
    List<Category> actual = service.addCategory(menu, request, "", randomUUID());

    // Then
    then(actual).hasSize(1);
    Category created = actual.get(0);
    then(created.getName()).isEqualTo(request.getName());
    then(created.getDescription()).isEqualTo(request.getDescription());
  }

  @Test
  void testEventsForCategoryCreation() {
    // Given a create category request is received

    // When the category is created
    CategoryService service = new CategoryService();
    List<MenuEvent> categoryEvents =
        service.categoryCreatedEvents(
            OperationCode.CREATE_CATEGORY.getCode(), "", randomUUID(), randomUUID());
    // Then
    then(categoryEvents).hasSize(2);
  }

  @Test
  void testEventsForCategoryUpdate() {
    // Given an update request for category is received

    // When the Category is updated
    CategoryService service = new CategoryService();
    List<MenuEvent> categoryEvents =
        service.categoryUpdatedEvents(
            OperationCode.UPDATE_CATEGORY.getCode(), "", randomUUID(), randomUUID());
    // Then
    then(categoryEvents).hasSize(2);
  }

  @Test
  void testEventsForCategoryDeletion() {
    // Given a Category deletion request is received

    // When the Category is deleted
    CategoryService service = new CategoryService();
    List<MenuEvent> categoryEvents =
        service.categoryDeletedEvents(
            OperationCode.DELETE_CATEGORY.getCode(), "", randomUUID(), randomUUID());
    // Then
    then(categoryEvents).hasSize(2);
  }
}
