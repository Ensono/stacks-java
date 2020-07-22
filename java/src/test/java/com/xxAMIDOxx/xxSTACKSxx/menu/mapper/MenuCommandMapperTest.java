package com.xxAMIDOxx.xxSTACKSxx.menu.mapper;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateCategoryRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateMenuRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.mapper.MenuCommandMapper;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuCommandMapperTest {

  @Test
  void createMenuRequestToCommand() {

    // Given
    String name = "xxx";
    String description = "yyy";
    UUID tenantId = UUID.randomUUID();
    Boolean enabled = true;
    CreateMenuRequest request = new CreateMenuRequest(name, description, tenantId, enabled);

    // When
    CreateMenuCommand command = MenuCommandMapper.INSTANCE.createMenuRequestToCommand(request);

    // Then
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
    assertEquals(tenantId, command.getRestaurantId());
    assertEquals(enabled, command.getEnabled());
  }

  @Test
  void createCategoryRequestToCommand() {

    // Given
    String name = "xxx";
    String description = "yyy";
    CreateCategoryRequest request = new CreateCategoryRequest(name, description);

    // When
    CreateCategoryCommand command = MenuCommandMapper.INSTANCE.createCategoryRequestToCommand(request);

    // Then
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
  }

  @Test
  void createItemRequestToCommand() {

    // Given
    String name = "xxx";
    String description = "yyy";
    Double price = 2.50;
    Boolean available = false;
    CreateItemRequest request = new CreateItemRequest(name, description, price, available);

    // When
    CreateItemCommand command = MenuCommandMapper.INSTANCE.createItemRequestToCommand(request);

    // Then
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
    assertEquals(price, command.getPrice());
    assertEquals(available, command.getAvailable());
  }
}
