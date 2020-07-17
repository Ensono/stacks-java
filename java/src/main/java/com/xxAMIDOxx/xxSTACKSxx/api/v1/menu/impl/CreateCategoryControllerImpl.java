package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command.CreateCategoryHandler;
import com.xxAMIDOxx.xxSTACKSxx.mapper.MenuCommandMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class CreateCategoryControllerImpl implements CreateCategoryController {

    private CreateCategoryHandler createCategoryHandler;

    public CreateCategoryControllerImpl(CreateCategoryHandler createCategoryHandler) {
        this.createCategoryHandler = createCategoryHandler;
    }

    @Override
    public ResponseEntity<ResourceCreatedResponse> addMenuCategory(
            UUID menuId,
            @Valid CreateCategoryRequest body,
            String correlationId) {

        CreateCategoryCommand command = MenuCommandMapper.INSTANCE.createCategoryRequestToCommand(body);
        command.setMenuId(menuId);
        command.setCorrelationId(correlationId);
        return new ResponseEntity<>(
                new ResourceCreatedResponse(createCategoryHandler.handle(command).get()),
                HttpStatus.CREATED);
    }
}
