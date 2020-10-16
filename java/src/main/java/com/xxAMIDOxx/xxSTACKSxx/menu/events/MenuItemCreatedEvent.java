package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {
  public MenuItemCreatedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    super(operationCode, correlationId, menuId, EventCode.MENU_ITEM_CREATED, categoryId, itemId);
  }
}
