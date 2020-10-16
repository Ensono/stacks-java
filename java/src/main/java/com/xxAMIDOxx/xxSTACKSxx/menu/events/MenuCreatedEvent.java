package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

  public MenuCreatedEvent(int operationCode, String correlationId, UUID menuId) {
    super(operationCode, correlationId, EventCode.MENU_CREATED, menuId);
  }
}
