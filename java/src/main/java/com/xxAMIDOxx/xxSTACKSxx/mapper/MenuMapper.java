package com.xxAMIDOxx.xxSTACKSxx.mapper;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.ItemDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
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
