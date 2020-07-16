package com.xxAMIDOxx.xxSTACKSxx.mapper;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requests.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateMenuCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuCommandMapper {

    MenuCommandMapper INSTANCE = Mappers.getMapper(MenuCommandMapper.class);

    @Mapping(source = "tenantId", target = "restaurantId")
    CreateMenuCommand createMenuRequestToCommand(CreateMenuRequest createMenuRequest);

}
