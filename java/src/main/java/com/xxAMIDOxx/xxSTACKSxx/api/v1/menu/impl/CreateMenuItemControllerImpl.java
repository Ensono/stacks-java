package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.CreateMenuItemController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command.CreateItemHandler;
import com.xxAMIDOxx.xxSTACKSxx.mapper.MenuCommandMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

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
        CreateItemCommand command = MenuCommandMapper.INSTANCE.createItemRequestToCommand(body);
        command.setMenuId(menuId);
        command.setCategoryId(categoryId);
        command.setCorrelationId(correlationId);
        return new ResponseEntity<>(
                new ResourceCreatedResponse(createItemHandler.handle(command).get()),
                HttpStatus.CREATED);
    }
}
