package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class CategoryEvent extends MenuEvent {
  private UUID categoryId;

  public CategoryEvent(
      int operationCode, String correlationId, UUID menuId, EventCode eventCode, UUID categoryId) {
    super(operationCode, correlationId, eventCode, menuId);
    this.categoryId = categoryId;
  }
}
