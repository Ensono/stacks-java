package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuUpdatedEvent extends MenuEvent {

  public MenuUpdatedEvent(int operationCode, String correlationId, UUID menuId) {
    super(operationCode, correlationId, EventCode.MENU_UPDATED, menuId);
  }
}
