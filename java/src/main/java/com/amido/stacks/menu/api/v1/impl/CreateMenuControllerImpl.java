package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.CreateMenuController;
import com.amido.stacks.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class CreateMenuControllerImpl implements CreateMenuController {

  public CreateMenuControllerImpl() {
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> createMenu(
      @Valid CreateMenuRequest body, String correlationId) {
    return new ResponseEntity<>(new ResourceCreatedResponse(UUID.randomUUID()), HttpStatus.CREATED);
  }
}
