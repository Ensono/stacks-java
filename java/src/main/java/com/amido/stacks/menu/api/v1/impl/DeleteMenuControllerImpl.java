package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.DeleteMenuController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

/**
 * DeleteMenuController implementation.
 *
 * @author ArathyKrishna
 */
@RestController
public class DeleteMenuControllerImpl implements DeleteMenuController {

  public DeleteMenuControllerImpl() {
  }

  @Override
  public ResponseEntity<Void> deleteMenu(UUID menuId, String correlationId) {
    return new ResponseEntity<>(OK);
  }
}
