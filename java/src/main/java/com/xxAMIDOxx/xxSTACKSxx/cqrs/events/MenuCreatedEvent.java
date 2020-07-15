package com.xxAMIDOxx.xxSTACKSxx.cqrs.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

    public MenuCreatedEvent(int operationCode, UUID correlationId, UUID menuId) {
        super(operationCode, correlationId, EventCode.MENU_CREATED, menuId);
    }

    public MenuCreatedEvent(OperationContext operationContext, UUID menuId) {
        super(operationContext, EventCode.MENU_CREATED, menuId);
    }
}
