package com.amido.stacks.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.menu.api.v1.DeleteCategoryController;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** @author ArathyKrishna */
@RestController
public class DeleteCategoryControllerImpl implements DeleteCategoryController {

  public DeleteCategoryControllerImpl() {}

  @Override
  public ResponseEntity<Void> deleteCategory(UUID menuId, UUID categoryId, String correlationId) {
    return new ResponseEntity<>(OK);
  }
}
