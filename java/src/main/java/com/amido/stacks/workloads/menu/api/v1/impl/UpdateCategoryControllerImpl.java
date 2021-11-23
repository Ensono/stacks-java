package com.amido.stacks.workloads.menu.api.v1.impl;

import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.workloads.menu.api.v1.UpdateCategoryController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for updating category.
 *
 * @author ArathyKrishna
 */
@RestController
public class UpdateCategoryControllerImpl implements UpdateCategoryController {

  public UpdateCategoryControllerImpl() {}

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenuCategory(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {
    return new ResponseEntity<>(new ResourceUpdatedResponse(UUID.randomUUID()), OK);
  }
}
