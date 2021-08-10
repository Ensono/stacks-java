package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.CreateCategoryController;
import com.amido.stacks.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  public CreateCategoryControllerImpl() {}

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    return new ResponseEntity<>(new ResourceCreatedResponse(UUID.randomUUID()), HttpStatus.CREATED);
  }
}
