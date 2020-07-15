package com.xxAMIDOxx.xxSTACKSxx.core.operations;

import java.util.UUID;

public abstract class OperationContext {

    private int operationCode;
    private UUID correlationId;

    public OperationContext(int operationCode, UUID correlationId) {
        this.operationCode = operationCode;
        this.correlationId = correlationId;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }
}
