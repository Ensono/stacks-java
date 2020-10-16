package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuDeletedEvent extends MenuEvent {

  public MenuDeletedEvent(int operationCode, String correlationId, UUID menuId) {
    super(operationCode, correlationId, EventCode.MENU_DELETED, menuId);
  }
}
