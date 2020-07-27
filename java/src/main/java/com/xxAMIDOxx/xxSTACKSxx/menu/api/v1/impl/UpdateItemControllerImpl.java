package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.UpdateItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.UpdateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.UpdateItemHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

/**
 * @author ArathyKrishna
 */

@RestController
public class UpdateItemControllerImpl implements UpdateItemController {

  private UpdateItemHandler handler;

  public UpdateItemControllerImpl(UpdateItemHandler handler) {
    this.handler = handler;
  }

  @Override
  public ResponseEntity<ResourceCreatedResponse> updateItem(UUID menuId,
                                                            UUID categoryId,
                                                            UUID itemId,
                                                            @Valid UpdateItemRequest body,
                                                            String correlationId) {

    UpdateItemCommand command =
            map(correlationId, menuId, categoryId, itemId, body);
    return new ResponseEntity<>(new ResourceCreatedResponse(handler.handle(command).get()),
            HttpStatus.OK);
  }
}
