package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.core.api.dto.ErrorResponse;
import com.amido.stacks.menu.api.v1.controller.CreateMenuController;
import com.amido.stacks.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceCreatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    UUID menuId = UUID.randomUUID();

    return new ResponseEntity<>(
        new ResourceCreatedResponse(menuId), HttpStatus.CREATED);
  }
}
