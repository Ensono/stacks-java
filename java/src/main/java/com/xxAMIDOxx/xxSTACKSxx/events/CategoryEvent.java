package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public abstract class CategoryEvent extends MenuEvent {
    private UUID categoryId;

    public CategoryEvent(OperationCode operationCode, String correlationId, EventCode eventCode, UUID menuId, UUID categoryId) {
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
