package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public abstract class MenuItemEvent extends MenuEvent {
    private UUID itemId;

    public MenuItemEvent(OperationCode operationCode, String correlationId, EventCode eventCode, UUID menuId, UUID itemId) {
        super(operationCode, correlationId, eventCode, menuId);
        this.itemId = itemId;
    }

    public MenuItemEvent(OperationContext operationContext, EventCode eventCode, UUID menuId, UUID itemId) {
        super(operationContext, eventCode, menuId);
        this.itemId = itemId;
    }

    public UUID getItemId() {
        return itemId;
    }
}
