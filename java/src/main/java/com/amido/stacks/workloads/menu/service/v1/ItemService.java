package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  public ResourceCreatedResponse create(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {
    return new ResourceCreatedResponse(UUID.randomUUID());
  }

  public ResourceUpdatedResponse update(
      UUID menuId, UUID categoryId, @Valid UpdateItemRequest body, String correlationId) {
    return new ResourceUpdatedResponse(UUID.randomUUID());
  }
}
