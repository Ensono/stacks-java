package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

  public CategoryCreatedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    super(operationCode, correlationId, menuId, EventCode.CATEGORY_CREATED, categoryId);
  }
}
