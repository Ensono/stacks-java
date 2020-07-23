package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainToDtoMapperTest {

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
    MenuDTO menuDTO = DomainToDtoMapper.toMenuDto(menu);

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
    SearchMenuResultItem resultItem = DomainToDtoMapper.toSearchMenuResultItem(menu);

    // Then
    assertThat(resultItem.getId()).isEqualTo(id);
    assertThat(resultItem.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(resultItem.getName()).isEqualTo(name);
    assertThat(resultItem.getDescription()).isEqualTo(description);
    assertThat(resultItem.getEnabled()).isEqualTo(enabled);

  }

}
