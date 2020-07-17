package com.xxAMIDOxx.xxSTACKSxx.mapper;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateMenuCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuCommandMapper {

    MenuCommandMapper INSTANCE = Mappers.getMapper(MenuCommandMapper.class);

    @Mapping(source = "tenantId", target = "restaurantId")
    CreateMenuCommand createMenuRequestToCommand(CreateMenuRequest createMenuRequest);

    CreateCategoryCommand createCategoryRequestToCommand(CreateCategoryRequest createCategoryRequest);
}
