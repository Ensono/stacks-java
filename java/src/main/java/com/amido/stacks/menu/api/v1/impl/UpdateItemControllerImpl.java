package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.UpdateItemController;
import com.amido.stacks.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceUpdatedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class UpdateItemControllerImpl implements UpdateItemController {

  public UpdateItemControllerImpl() {
  }

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateItem(UUID menuId, UUID categoryId, UUID itemId,
                                                            @Valid UpdateItemRequest body, String correlationId) {
    return new ResponseEntity<>(new ResourceUpdatedResponse(itemId), HttpStatus.OK);
  }
}
