package com.amido.stacks.workloads.menu.exception;

public class MenuApiException extends RuntimeException {

  private static final int EXCEPTION_CODE = 10000;

  public MenuApiException(String message) {
    super(message);
  }

  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
