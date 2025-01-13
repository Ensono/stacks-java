package com.ensono.stacks.core.operations;

public abstract class OperationContext {

    private String correlationId;

    protected OperationContext(final String correlationId) {
        this.correlationId = correlationId;
    }

    public abstract int getOperationCode();

    /** No arg constructor. */
    protected OperationContext() {
        // NO-OP
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(final String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        return String.format(
                "OperationContext{operationCode=%d, correlationId=%s}", getOperationCode(), correlationId);
    }
}
