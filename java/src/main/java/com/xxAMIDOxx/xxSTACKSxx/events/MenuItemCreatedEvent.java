package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {
    public MenuItemCreatedEvent(OperationCode operationCode, String correlationId, UUID menuId, UUID itemId) {
        super(operationCode, correlationId, EventCode.MENU_ITEM_CREATED, menuId, itemId);
    }

    public MenuItemCreatedEvent(OperationContext operationContext, UUID menuId, UUID itemId) {
        super(operationContext, EventCode.MENU_ITEM_CREATED, menuId, itemId);
    }
}
