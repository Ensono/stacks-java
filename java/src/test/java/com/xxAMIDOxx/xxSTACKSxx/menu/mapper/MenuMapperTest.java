package com.xxAMIDOxx.xxSTACKSxx.menu.mapper;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuMapperTest {

  @Test
  void menuToMenuDto() {

    // Given
    UUID id = UUID.randomUUID();
    UUID restaurantId = UUID.randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    Menu menu = new Menu(id.toString(), restaurantId.toString(), name, description,
            Collections.emptyList(), enabled);

    // When
    MenuDTO menuDTO = MenuMapper.INSTANCE.menuToMenuDto(menu);

    // Then
    assertThat(menuDTO.getId()).isEqualTo(id);
    assertThat(menuDTO.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(menuDTO.getName()).isEqualTo(name);
    assertThat(menuDTO.getDescription()).isEqualTo(description);
    assertThat(menuDTO.getCategories()).isEqualTo(Collections.emptyList());
    assertThat(menuDTO.getEnabled()).isEqualTo(enabled);
  }

  @Test
  void menuToSearchMenuResultItem() {

    // Given
    UUID id = UUID.randomUUID();
    UUID restaurantId = UUID.randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    Boolean enabled = true;

    Menu menu = new Menu(id.toString(), restaurantId.toString(), name, description,
            null, enabled);

    // When
    SearchMenuResultItem resultItem = MenuMapper.INSTANCE.menuToSearchMenuResultItem(menu);

    // Then
    assertThat(resultItem.getId()).isEqualTo(id);
    assertThat(resultItem.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(resultItem.getName()).isEqualTo(name);
    assertThat(resultItem.getDescription()).isEqualTo(description);
    assertThat(resultItem.getEnabled()).isEqualTo(enabled);

  }

}
