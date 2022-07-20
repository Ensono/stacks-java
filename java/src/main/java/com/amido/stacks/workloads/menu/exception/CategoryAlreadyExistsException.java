package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class CategoryAlreadyExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 11409;

  public CategoryAlreadyExistsException(UUID menuId, String name) {
    super(
        String.format(
            "A category with the name '%s' already exists for the menu with id '%s'.",
            name, menuId));
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
