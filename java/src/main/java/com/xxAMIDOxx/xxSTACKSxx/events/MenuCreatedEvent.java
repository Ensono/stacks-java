package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

    public MenuCreatedEvent(OperationCode operationCode, String correlationId, UUID menuId) {
        super(operationCode, correlationId, EventCode.MENU_CREATED, menuId);
    }

    public MenuCreatedEvent(OperationContext operationContext, UUID menuId) {
        super(operationContext, EventCode.MENU_CREATED, menuId);
    }
}
