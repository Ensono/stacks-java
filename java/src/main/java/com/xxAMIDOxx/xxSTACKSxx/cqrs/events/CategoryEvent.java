package com.xxAMIDOxx.xxSTACKSxx.cqrs.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public abstract class CategoryEvent extends MenuEvent {
    private UUID categoryId;

    public CategoryEvent(int operationCode, UUID correlationId, EventCode eventCode, UUID menuId, UUID categoryId) {
        super(operationCode, correlationId, eventCode, menuId);
        this.categoryId = categoryId;
    }

    public CategoryEvent(OperationContext operationContext, EventCode eventCode, UUID menuId, UUID categoryId) {
        super(operationContext, eventCode, menuId);
        this.categoryId = categoryId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }
}
