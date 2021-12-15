package com.amido.stacks.workloads.menu.api.v1.impl;

import com.amido.stacks.workloads.menu.api.v1.UpdateMenuController;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ResourceUpdatedResponse;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateMenuControllerImpl implements UpdateMenuController {

  public UpdateMenuControllerImpl() {}

  @Override
  public ResponseEntity<ResourceUpdatedResponse> updateMenu(
      UUID menuId, @Valid UpdateMenuRequest body, String correlationId) {
    return new ResponseEntity<>(new ResourceUpdatedResponse(UUID.randomUUID()), HttpStatus.OK);
  }
}
