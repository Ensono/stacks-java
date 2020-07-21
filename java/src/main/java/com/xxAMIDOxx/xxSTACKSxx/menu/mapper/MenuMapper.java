package com.xxAMIDOxx.xxSTACKSxx.menu.mapper;

import com.xxAMIDOxx.xxSTACKSxx.core.mapper.UuidMapper;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UuidMapper.class)
public interface MenuMapper {

    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    SearchMenuResultItem menuToSearchMenuResultItem(Menu menu);

    MenuDTO menuToMenuDto(Menu menu);
}
