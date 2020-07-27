package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.DeleteCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.DeleteCategoryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class DeleteCategoryControllerImpl implements DeleteCategoryController {

  private DeleteCategoryHandler handler;

  public DeleteCategoryControllerImpl(DeleteCategoryHandler handler) {
    this.handler = handler;
  }

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId,
                                             String correlationId) {
    DeleteCategoryCommand command = map(correlationId, menuId, categoryId);
    handler.handle(command);
    return new ResponseEntity<>(OK);
  }
}
