package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import java.util.Arrays;
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
  private final ApplicationEventPublisher publisher;

  public DeleteCategoryControllerImpl(
      MenuQueryService menuQueryService,
      CategoryService categoryService,
      ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.publisher = publisher;
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

    createAndPublishEvents(operationCode, correlationId, menuId, categoryId);

    return new ResponseEntity<>(OK);
  }

  /**
   * create and publish event.
   *
   * @param operationCode operationCode
   * @param correlationId correlationId
   * @param menuId menu id
   * @param categoryId categoryId
   */
  public void createAndPublishEvents(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    List<MenuEvent> eventList =
        Arrays.asList(
            new MenuUpdatedEvent(operationCode, correlationId, menuId),
            new CategoryDeletedEvent(operationCode, correlationId, menuId, categoryId));

    eventList.forEach(publisher::publish);
  }
}
