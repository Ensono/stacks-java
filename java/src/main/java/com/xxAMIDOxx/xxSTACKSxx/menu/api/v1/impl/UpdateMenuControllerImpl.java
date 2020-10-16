package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.UpdateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateMenuControllerImpl implements UpdateMenuController {

  private final MenuQueryService menuQueryService;
  private final ApplicationEventPublisher publisher;

  public UpdateMenuControllerImpl(
      MenuQueryService menuQueryService, ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.publisher = publisher;
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenu(
      UUID menuId, @Valid UpdateMenuRequest body, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.UPDATE_MENU.getCode(), correlationId);

    menu.setName(body.getName());
    menu.setDescription(body.getDescription());
    menu.setEnabled(body.getEnabled());

    UUID id = menuQueryService.update(menu);

    // publish event
    createAndPublishEvents(
        OperationCode.UPDATE_MENU.getCode(), correlationId, UUID.fromString(menu.getId()));

    return new ResponseEntity<>(new ResourceUpdatedResponse(id), HttpStatus.OK);
  }

  protected void createAndPublishEvents(int operationCode, String correlationId, UUID menuId) {
    List<MenuEvent> eventList =
        Collections.singletonList(new MenuUpdatedEvent(operationCode, correlationId, menuId));

    eventList.forEach(publisher::publish);
  }
}
