package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class CategoryDoesNotExistException extends MenuApiException {

  private static final int EXCEPTION_CODE = 11404;

  public CategoryDoesNotExistException(UUID menuId, UUID categoryId) {
    super(
        String.format(
            "A category with the id '%s' does not exist for menu with id '%s'.",
            categoryId, menuId));
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
