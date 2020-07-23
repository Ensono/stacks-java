package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.CreateCategoryController;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ResourceCreatedResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.CreateCategoryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.mappers.RequestToCommandMapper.map;

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

        CreateCategoryCommand command = map(correlationId, menuId, body);
        return new ResponseEntity<>(
                new ResourceCreatedResponse(createCategoryHandler.handle(command).get()),
                HttpStatus.CREATED);
    }
}
