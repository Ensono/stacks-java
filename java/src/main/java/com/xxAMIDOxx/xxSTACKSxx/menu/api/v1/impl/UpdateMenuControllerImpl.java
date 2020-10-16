package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.controller.UpdateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateMenuControllerImpl implements UpdateMenuController {

  private final MenuQueryService menuQueryService;

  public UpdateMenuControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
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
    menuQueryService.publishEvents(
        menuQueryService.createMenuUpdatedEvents(
            OperationCode.UPDATE_MENU.getCode(), correlationId, id));

    return new ResponseEntity<>(new ResourceUpdatedResponse(id), HttpStatus.OK);
  }
}
