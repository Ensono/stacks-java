package com.amido.stacks.core.api.exception;

public class ApiException extends RuntimeException {

  String correlationId;

  public ApiException(
      String message,
      String correlationId) {
    super(message);
    this.correlationId = correlationId;
  }

  public String getCorrelationId() {
    return correlationId;
  }
}
