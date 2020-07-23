package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateMenuItemController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.CreateItemHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

@RestController
public class CreateMenuItemControllerImpl implements CreateMenuItemController {

    private CreateItemHandler createItemHandler;

    public CreateMenuItemControllerImpl(CreateItemHandler createItemHandler) {
        this.createItemHandler = createItemHandler;
    }

    @Override
    public ResponseEntity<ResourceCreatedResponse> addMenuItem(UUID menuId,
                                                               UUID categoryId,
                                                               @Valid CreateItemRequest body,
                                                               String correlationId) {

        CreateItemCommand command = map(correlationId, menuId, categoryId, body);
        return new ResponseEntity<>(
                new ResourceCreatedResponse(createItemHandler.handle(command).get()),
                HttpStatus.CREATED);
    }
}
