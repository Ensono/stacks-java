package com.xxAMIDOxx.xxSTACKSxx.menu.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.CreateItemRequest;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateItemCommand;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class RequestToCommandMapperTest {

  @Test
  void createItemRequestToCommand() {

    // Given
    String correlationId = "ccc";
    UUID menuId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    String name = "xxx";
    String description = "yyy";
    Double price = 2.50;
    Boolean available = false;
    CreateItemRequest request = new CreateItemRequest(name, description, price, available);

    // When
    CreateItemCommand command =
        RequestToCommandMapper.map(correlationId, menuId, categoryId, request);

    // Then
    assertEquals(correlationId, command.getCorrelationId());
    assertEquals(menuId, command.getMenuId());
    assertEquals(categoryId, command.getCategoryId());
    assertEquals(name, command.getName());
    assertEquals(description, command.getDescription());
    assertEquals(price, command.getPrice());
    assertEquals(available, command.getAvailable());
  }
}
