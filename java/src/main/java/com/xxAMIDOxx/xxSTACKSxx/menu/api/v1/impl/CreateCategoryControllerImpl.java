package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.impl.CategoryService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  private final MenuQueryService menuQueryService;
  private final CategoryService categoryService;
  private final ApplicationEventPublisher publisher;

  public CreateCategoryControllerImpl(
      MenuQueryService menuQueryService,
      CategoryService categoryService,
      ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.categoryService = categoryService;
    this.publisher = publisher;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.CREATE_CATEGORY.getCode(), correlationId);

    UUID categoryId = UUID.randomUUID();

    menu.setCategories(categoryService.addCategory(menu, body, correlationId, categoryId));

    menuQueryService.update(menu);

    createAndPublishEvents(
        OperationCode.CREATE_CATEGORY.getCode(), correlationId, menuId, categoryId);

    return new ResponseEntity<>(new ResourceCreatedResponse(categoryId), HttpStatus.CREATED);
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
            new CategoryCreatedEvent(operationCode, correlationId, menuId, categoryId));

    eventList.forEach(publisher::publish);
  }
}
