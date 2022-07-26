package com.amido.stacks.workloads.menu.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class MenuTest {

  @Test
  void shouldBuildMenu() {

    UUID id = UUID.randomUUID();
    UUID restId = UUID.randomUUID();

    Menu menu =
        Menu.builder()
            .id(id.toString())
            .restaurantId(restId.toString())
            .name("menu name")
            .description("menu desc")
            .enabled(true)
            .build();

    assertEquals(id.toString(), menu.getId());
    assertEquals(restId.toString(), menu.getRestaurantId());
    assertEquals("menu name", menu.getName());
    assertEquals("menu desc", menu.getDescription());
    assertTrue(menu.getEnabled());
  }
}
