package com.xxAMIDOxx.xxSTACKSxx.exception;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

public class ApiException extends RuntimeException {

    ExceptionCode exceptionCode;
    OperationCode operationCode;
    String correlationId;

    public ApiException(String message, ExceptionCode exceptionCode, OperationCode operationCode, String correlationId) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.operationCode = operationCode;
        this.correlationId = correlationId;
    }
}
