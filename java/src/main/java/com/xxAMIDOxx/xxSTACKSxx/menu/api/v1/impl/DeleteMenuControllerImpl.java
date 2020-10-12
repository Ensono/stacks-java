package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;

import java.util.UUID;

import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * DeleteMenuController implementation.
 */
@RestController
public class DeleteMenuControllerImpl implements DeleteMenuController {

  private MenuQueryService menuQueryService;

  public DeleteMenuControllerImpl(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<Void> deleteMenu(UUID menuId, String correlationId) {

    Menu menu = menuQueryService.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(menuId.toString(),
                    OperationCode.DELETE_MENU.getCode(),
                    correlationId));

    menuQueryService.delete(menu);

    return new ResponseEntity<>(OK);
  }
}
