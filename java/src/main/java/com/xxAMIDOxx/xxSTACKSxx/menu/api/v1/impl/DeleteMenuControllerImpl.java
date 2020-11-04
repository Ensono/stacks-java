package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.DeleteMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteMenuControllerImpl implements DeleteMenuController {

  private final MenuQueryService menuQueryService;

  public DeleteMenuControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<Void> deleteMenu(UUID menuId, String correlationId) {

    Menu menu =
        menuQueryService.findMenuOrThrowException(
            menuId, OperationCode.DELETE_MENU.getCode(), correlationId);

    menuQueryService.delete(menu);

    // publish event
    menuQueryService.publishEvents(
        menuQueryService.menuDeletedEvents(
            OperationCode.DELETE_MENU.getCode(), correlationId, UUID.fromString(menu.getId())));

    return new ResponseEntity<>(OK);
  }
}
