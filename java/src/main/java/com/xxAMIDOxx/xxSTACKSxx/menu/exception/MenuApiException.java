package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.core.api.exception.ApiException;
import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationCode;

public class MenuApiException extends ApiException {

  public MenuApiException(
      String message, ExceptionCode exceptionCode, int operationCode, String correlationId) {
    super(message, exceptionCode, OperationCode.fromCode(operationCode), correlationId);
  }
}
