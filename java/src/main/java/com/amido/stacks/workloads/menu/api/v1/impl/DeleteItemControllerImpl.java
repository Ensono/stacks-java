package com.amido.stacks.workloads.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.workloads.menu.api.v1.DeleteItemController;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** @author ArathyKrishna */
@RestController
public class DeleteItemControllerImpl implements DeleteItemController {

  public DeleteItemControllerImpl() {}

  @Override
  public ResponseEntity<Void> deleteItem(
      UUID menuId, UUID categoryId, UUID itemId, String correlationId) {
    return new ResponseEntity<>(OK);
  }
}
