package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuItemUpdatedEvent extends MenuItemEvent {

  public MenuItemUpdatedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    super(operationCode, correlationId, menuId, EventCode.MENU_ITEM_UPDATED, categoryId, itemId);
  }
}
