package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteMenuControllerImpl implements DeleteMenuController {

  private final MenuQueryService menuQueryService;
  private final ApplicationEventPublisher publisher;

  public DeleteMenuControllerImpl(
      MenuQueryService menuQueryService, ApplicationEventPublisher publisher) {
    this.menuQueryService = menuQueryService;
    this.publisher = publisher;
  }

  @Override
  public ResponseEntity<Void> deleteMenu(UUID menuId, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.DELETE_MENU.getCode(), correlationId);

    menuQueryService.delete(menu);

    // publish event
    createAndPublishEvents(
        OperationCode.DELETE_MENU.getCode(), correlationId, UUID.fromString(menu.getId()));

    return new ResponseEntity<>(OK);
  }

  protected void createAndPublishEvents(int operationCode, String correlationId, UUID menuId) {
    List<MenuEvent> eventList =
        Collections.singletonList(new MenuDeletedEvent(operationCode, correlationId, menuId));

    eventList.forEach(publisher::publish);
  }
}
