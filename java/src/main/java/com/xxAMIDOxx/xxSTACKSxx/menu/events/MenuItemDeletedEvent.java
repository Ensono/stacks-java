package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuItemDeletedEvent extends MenuItemEvent {

  public MenuItemDeletedEvent(
      int operationCode, String correlationId, UUID menuId, UUID categoryId, UUID itemId) {
    super(operationCode, correlationId, menuId, EventCode.MENU_ITEM_DELETED, categoryId, itemId);
  }
}
