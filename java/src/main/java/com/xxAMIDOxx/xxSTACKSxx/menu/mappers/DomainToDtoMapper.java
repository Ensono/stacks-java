package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.CategoryDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.ItemDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DomainToDtoMapper {

    public static MenuDTO toMenuDto(Menu menu) {
        MenuDTO dto = new MenuDTO();
        if (menu.getCategories() == null || menu.getCategories().isEmpty()) {
            dto.setCategories(Collections.emptyList());
        } else {
            dto.setCategories(menu.getCategories().stream().filter(Objects::nonNull).map(
                    DomainToDtoMapper::toCategoryDto)
                    .collect(Collectors.toList()));

        }

        dto.setEnabled(menu.getEnabled());
        dto.setName(menu.getName());
        dto.setDescription(menu.getDescription());
        dto.setId(UUID.fromString(menu.getId()));
        return dto;
    }

    public static CategoryDTO toCategoryDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        if (category.getItems() == null || category.getItems().isEmpty()) {
            dto.setItems(Collections.emptyList());
        } else {
            dto.setItems(category.getItems().stream().filter(Objects::nonNull)
                    .map(DomainToDtoMapper::toItemDto)
                    .collect(Collectors.toList()));
        }
        dto.setDescription(category.getDescription());
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public static ItemDTO toItemDto(Item item) {
        return new ItemDTO(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAvailable());
    }

    public static SearchMenuResultItem toSearchMenuResultItem(Menu menu) {
        return new SearchMenuResultItem(UUID.fromString(menu.getId()),
                UUID.fromString(menu.getRestaurantId()),
                menu.getName(),
                menu.getDescription(),
                menu.getEnabled());
    }
}
