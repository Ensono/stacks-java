package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public abstract class MenuItemEvent extends CategoryEvent {
  private UUID itemId;

  public MenuItemEvent(
      int operationCode,
      String correlationId,
      UUID menuId,
      EventCode eventCode,
      UUID categoryId,
      UUID itemId) {
    super(operationCode, correlationId, menuId, eventCode, categoryId);
    this.itemId = itemId;
  }

  public UUID getItemId() {
    return itemId;
  }

  @Override
  public String toString() {
    return "MenuItemEvent{" + "itemId=" + itemId + "} " + super.toString();
  }
}
