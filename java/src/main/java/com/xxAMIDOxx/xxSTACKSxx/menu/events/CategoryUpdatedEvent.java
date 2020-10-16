package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class CategoryUpdatedEvent extends CategoryEvent {

  public CategoryUpdatedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    super(operationCode, correlationId, menuId, EventCode.CATEGORY_UPDATED, categoryId);
  }
}
