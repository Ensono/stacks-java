package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class CategoryDeletedEvent extends CategoryEvent {

  public CategoryDeletedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId) {
    super(operationCode, correlationId, menuId, EventCode.CATEGORY_DELETED, categoryId);
  }
}
