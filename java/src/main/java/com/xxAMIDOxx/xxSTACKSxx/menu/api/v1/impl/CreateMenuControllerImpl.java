package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateMenuController;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.command.CreateMenuHandler;
import com.xxAMIDOxx.xxSTACKSxx.menu.mapper.MenuCommandMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CreateMenuControllerImpl implements CreateMenuController {

    private CreateMenuHandler createMenuHandler;

    public CreateMenuControllerImpl(CreateMenuHandler createMenuHandler) {
        this.createMenuHandler = createMenuHandler;
    }

    @Override
    public ResponseEntity<ResourceCreatedResponse> createMenu(@Valid CreateMenuRequest body,
                                                              String correlationId) {

        CreateMenuCommand command = MenuCommandMapper.INSTANCE.createMenuRequestToCommand(body);
        command.setCorrelationId(correlationId);
        return new ResponseEntity<>(
                new ResourceCreatedResponse(createMenuHandler.handle(command).get()),
                HttpStatus.CREATED);
    }
}
