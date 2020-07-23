package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;

import java.util.UUID;

public class RequestToCommandMapper {

    public static CreateMenuCommand map(String correlationId, CreateMenuRequest r) {
        return new CreateMenuCommand(correlationId, r.getName(), r.getDescription(),
                r.getTenantId(), r.getEnabled());
    }

    public static CreateCategoryCommand map(String correlationId, UUID menuId, CreateCategoryRequest r) {
        return new CreateCategoryCommand(correlationId, menuId, r.getName(), r.getDescription());
    }

    public static CreateItemCommand map(String correlationId, UUID menuId, UUID categoryId, CreateItemRequest r) {
        return new CreateItemCommand(correlationId, menuId, categoryId, r.getName(),
                r.getDescription(), r.getPrice(), r.getAvailable());
    }
}
