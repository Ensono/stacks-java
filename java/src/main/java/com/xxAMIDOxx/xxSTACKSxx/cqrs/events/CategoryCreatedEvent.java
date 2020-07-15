package com.xxAMIDOxx.xxSTACKSxx.cqrs.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

    public CategoryCreatedEvent(int operationCode, UUID correlationId, UUID menuId, UUID categoryId) {
        super(operationCode, correlationId, EventCode.CATEGORY_CREATED, menuId, categoryId);
    }

    public CategoryCreatedEvent(OperationContext operationContext, UUID menuId, UUID categoryId) {
        super(operationContext, EventCode.CATEGORY_CREATED, menuId, categoryId);
    }
}
