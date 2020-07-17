package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class MenuUpdatedEvent extends MenuEvent {

    public MenuUpdatedEvent(OperationCode operationCode, String correlationId, UUID menuId) {
        super(operationCode, correlationId, EventCode.MENU_UPDATED, menuId);
    }

    public MenuUpdatedEvent(OperationContext operationContext, UUID menuId) {
        super(operationContext, EventCode.MENU_UPDATED, menuId);
    }
}
