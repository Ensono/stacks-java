package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class ItemAlreadyExistsException extends RuntimeException {

  private static final int EXCEPTION_CODE = 12409;

  public ItemAlreadyExistsException(UUID menuId, UUID categoryId, String name) {
    super(
        String.format(
            "An item with the name '%s' already exists for the category '%s' in menu with "
                + "id '%s'.",
            name, categoryId, menuId));
  }

  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
