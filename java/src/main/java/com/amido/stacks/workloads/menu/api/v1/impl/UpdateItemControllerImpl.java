package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.workloads.menu.api.v1.UpdateItemController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** @author ArathyKrishna */
@RestController
public class UpdateItemControllerImpl implements UpdateItemController {

  public UpdateItemControllerImpl() {}

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateItem(
      UUID menuId,
      UUID categoryId,
      UUID itemId,
      @Valid UpdateItemRequest body,
      String correlationId) {
    return new ResponseEntity<>(new ResourceUpdatedResponse(UUID.randomUUID()), HttpStatus.OK);
  }
}
