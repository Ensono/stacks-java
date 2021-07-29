package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.UpdateCategoryController;
import com.amido.stacks.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceUpdatedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

/** Controller for updating category. */
@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  public UpdateCategoryControllerImpl() {
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {
    return new ResponseEntity<>(new ResourceUpdatedResponse(categoryId), OK);
  }
}
