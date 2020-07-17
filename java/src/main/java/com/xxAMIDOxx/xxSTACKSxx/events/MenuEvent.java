package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEvent;
import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public abstract class MenuEvent extends ApplicationEvent {

    private UUID menuId;

    public MenuEvent(final OperationCode operationCode,
                     final String correlationId,
                     final EventCode eventCode,
                     final UUID menuId) {
        super(operationCode.getCode(), correlationId, eventCode.getCode());
        this.menuId = menuId;
    }

    public MenuEvent(final OperationContext operationContext,
                     final EventCode eventCode,
                     final UUID menuId) {
        super(operationContext.getOperationCode(),
                operationContext.getCorrelationId(),
                eventCode.getCode());
        this.menuId = menuId;
    }

    public UUID getMenuId() {
        return menuId;
    }

    @Override
    public String toString() {
        return "MenuEvent{" +
                "menuId=" + menuId +
                "} " + super.toString();
    }
}
