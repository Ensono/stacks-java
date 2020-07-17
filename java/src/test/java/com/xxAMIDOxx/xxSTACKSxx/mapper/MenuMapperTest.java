package com.xxAMIDOxx.xxSTACKSxx.mapper;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MenuMapperTest {

  @Test
  void menuToMenuDto() {

    UUID id = UUID.randomUUID();
    UUID restaurantId = UUID.randomUUID();
    String name = "yyyyyyyy";
    String description = "xxxxxxxx";
    List<Category> categories = Collections.emptyList();

    Menu menu = new Menu(id.toString(), restaurantId.toString(), name, description, Collections.emptyList(), true);
    MenuDTO menuDTO = MenuMapper.INSTANCE.menuToMenuDto(menu);

    assertThat(menuDTO.getId()).isEqualTo(id);
    assertThat(menuDTO.getRestaurantId()).isEqualTo(restaurantId);
    assertThat(menuDTO.getName()).isEqualTo(name);
    assertThat(menuDTO.getDescription()).isEqualTo(description);
    assertThat(menuDTO.getCategories()).isEqualTo(Collections.emptyList());
  }

}
