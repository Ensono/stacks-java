package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.CreateItemController;
import com.amido.stacks.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class CreateItemControllerImpl implements CreateItemController {

  public CreateItemControllerImpl() { }

  @Override
  public ResponseEntity<ResourceCreatedResponse> addMenuItem(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {

    UUID itemId = UUID.randomUUID();

    return new ResponseEntity<>(new ResourceCreatedResponse(itemId), HttpStatus.CREATED);
  }
}
