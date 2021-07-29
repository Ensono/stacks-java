package com.amido.stacks.menu.exception;

import com.amido.stacks.core.api.exception.ApiException;
import com.amido.stacks.core.operations.OperationCode;

public class MenuApiException extends ApiException {

  public MenuApiException(String message, ExceptionCode exceptionCode, int operationCode, String correlationId) {
    super(message, exceptionCode, OperationCode.fromCode(operationCode), correlationId);
  }
}
