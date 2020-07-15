package com.xxAMIDOxx.xxSTACKSxx.core.events;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public abstract class ApplicationEvent extends OperationContext {

    private int eventCode;

    public ApplicationEvent(int operationCode, UUID correlationId, int eventCode) {
        super(operationCode, correlationId);
        this.eventCode = eventCode;
    }

    public int getEventCode() {
        return eventCode;
    }
}
