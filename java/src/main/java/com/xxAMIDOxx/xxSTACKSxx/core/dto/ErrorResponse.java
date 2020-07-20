package com.xxAMIDOxx.xxSTACKSxx.core.dto;

public class ErrorResponse {

    int errorCode;
    int operationCode;
    String correlationId;
    String description;

    public ErrorResponse(int errorCode, int operationCode, String correlationId, String description) {
        this.errorCode = errorCode;
        this.operationCode = operationCode;
        this.correlationId = correlationId;
        this.description = description;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getOperationCode() {
        return operationCode;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getDescription() {
        return description;
    }
}
