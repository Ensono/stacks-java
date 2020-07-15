package com.xxAMIDOxx.xxSTACKSxx.cqrs.events;

import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEvent;
import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public abstract class MenuEvent extends ApplicationEvent {

    private UUID menuId;

    public MenuEvent(final int operationCode,
                     final UUID correlationId,
                     final EventCode eventCode,
                     final UUID menuId) {
        super(operationCode, correlationId, eventCode.getCode());
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
}
