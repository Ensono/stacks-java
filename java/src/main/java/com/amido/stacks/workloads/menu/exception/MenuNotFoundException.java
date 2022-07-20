package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class MenuNotFoundException extends MenuApiException {

  private static final int EXCEPTION_CODE = 10404;

  public MenuNotFoundException(UUID menuId) {
    super(String.format("A menu with id '%s' does not exist.", menuId));
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
