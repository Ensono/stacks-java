package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.event.ApplicationEvent;
import java.util.UUID;

public abstract class MenuEvent extends ApplicationEvent {

  private UUID menuId;

  public MenuEvent(
      int operationCode, String correlationId, EventCode eventCode, final UUID menuId) {
    super(operationCode, correlationId, eventCode.getCode());
    this.menuId = menuId;
  }

  @Override
  public String toString() {
    return "MenuEvent{" + "menuId=" + menuId + "} " + super.toString();
  }
}
