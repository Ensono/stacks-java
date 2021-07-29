package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.DeleteItemController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class DeleteItemControllerImpl implements DeleteItemController {

  public DeleteItemControllerImpl() {
  }

  @Override
  public ResponseEntity<Void> deleteItem(UUID menuId, UUID categoryId, UUID itemId, String correlationId) {
    return new ResponseEntity<>(OK);
  }
}
