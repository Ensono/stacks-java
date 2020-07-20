package com.xxAMIDOxx.xxSTACKSxx.menu.mapper;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuCommandMapper {

    MenuCommandMapper INSTANCE = Mappers.getMapper(MenuCommandMapper.class);

    @Mapping(source = "tenantId", target = "restaurantId")
    CreateMenuCommand createMenuRequestToCommand(CreateMenuRequest createMenuRequest);

    CreateCategoryCommand createCategoryRequestToCommand(CreateCategoryRequest createCategoryRequest);

    CreateItemCommand createItemRequestToCommand(CreateItemRequest createItemRequest);
}
