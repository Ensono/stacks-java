package com.amido.stacks.core.api.exception;

import com.amido.stacks.core.operations.OperationCode;
import com.amido.stacks.menu.exception.ExceptionCode;

public class ApiException extends RuntimeException {

  final ExceptionCode exceptionCode;
  final OperationCode operationCode;
  final String correlationId;

  public ApiException(
      String message,
      ExceptionCode exceptionCode,
      OperationCode operationCode,
      String correlationId) {
    super(message);
    this.exceptionCode = exceptionCode;
    this.operationCode = operationCode;
    this.correlationId = correlationId;
  }

  public ExceptionCode getExceptionCode() {
    return exceptionCode;
  }

  public OperationCode getOperationCode() {
    return operationCode;
  }

  public String getCorrelationId() {
    return correlationId;
  }
}
