package com.amido.stacks.workloads.menu.exception;

import com.amido.stacks.core.api.exception.ApiException;

public class MenuApiException extends ApiException {

  private static final int EXCEPTION_CODE = 10000;

  public MenuApiException(String message) {
    super(message, 0, "");
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
