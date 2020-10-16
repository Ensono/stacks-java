package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.CreateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateMenuControllerImpl implements CreateMenuController {

  private final MenuQueryService menuQueryService;
  private final ApplicationEventPublisher publisher;

  public CreateMenuControllerImpl(
      MenuQueryService menuQueryService, ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.publisher = publisher;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid CreateMenuRequest body, String correlationId) {

    String restaurantId = body.getTenantId().toString();
    String name = body.getName();

    verifyMenuNotAlreadyExisting(restaurantId, name, correlationId);

    final Menu menu =
        new Menu(
            UUID.randomUUID().toString(),
            restaurantId,
            name,
            body.getDescription(),
            new ArrayList<>(),
            body.getEnabled());

    menuQueryService.create(menu);

    // publish event
    createAndPublishEvents(
        OperationCode.CREATE_MENU.getCode(), correlationId, UUID.fromString(menu.getId()));

    return new ResponseEntity<>(
        new ResourceCreatedResponse(UUID.fromString(menu.getId())), HttpStatus.CREATED);
  }

  protected void verifyMenuNotAlreadyExisting(
      String restaurantId, String name, String correlationId) {
    List<Menu> existing =
        menuQueryService.findAllByRestaurantIdAndName(UUID.fromString(restaurantId), name, 1, 0);

    if (!existing.isEmpty() && existing.stream().anyMatch(m -> m.getName().equals(name))) {
      throw new MenuAlreadyExistsException(
          restaurantId, name, OperationCode.CREATE_MENU.getCode(), correlationId);
    }
  }

  protected void createAndPublishEvents(int operationCode, String correlationId, UUID menuId) {
    List<MenuEvent> eventList =
        Collections.singletonList(new MenuCreatedEvent(operationCode, correlationId, menuId));

    eventList.forEach(publisher::publish);
  }
}
