package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

    public CategoryCreatedEvent(OperationCode operationCode, String correlationId, UUID menuId, UUID categoryId) {
        super(operationCode, correlationId, EventCode.CATEGORY_CREATED, menuId, categoryId);
    }

    public CategoryCreatedEvent(OperationContext operationContext, UUID menuId, UUID categoryId) {
        super(operationContext, EventCode.CATEGORY_CREATED, menuId, categoryId);
    }
}
