package com.xxAMIDOxx.xxSTACKSxx.menu.mapper;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ItemDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.core.mapper.UuidMapper;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UuidMapper.class)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    MenuDTO menuToMenuDto(Menu menu);

    CategoryDTO categoryTocategoryDto(Category category);

    ItemDTO itemToItemDto(Item item);

    Item itemDtoToItem(ItemDTO item);

    Menu menuDtoToMenu(MenuDTO menu);
}
