package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class MenuAlreadyExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 10409;

  public MenuAlreadyExistsException(UUID restaurantId, String name) {
    super(
        String.format(
            "A Menu with the name '%s' already exists for the restaurant with id '%s'.",
            name, restaurantId));
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
