package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.CreateCategoryController;
import com.amido.stacks.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

  public CreateCategoryControllerImpl() {
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    UUID categoryId = UUID.randomUUID();

    return new ResponseEntity<>(new ResourceCreatedResponse(categoryId), HttpStatus.CREATED);
  }
}
