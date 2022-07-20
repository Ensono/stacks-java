package com.amido.stacks.workloads.menu.exception;

import java.util.UUID;

public class ItemDoesNotExistsException extends MenuApiException {

  private static final int EXCEPTION_CODE = 12404;

  public ItemDoesNotExistsException(UUID menuId, UUID categoryId, UUID itemId) {
    super(
        String.format(
            "An item with the id '%s' does not exists for category with the id '%s' and for menu with id '%s'.",
            itemId, categoryId, menuId));
  }

  @Override
  public int getExceptionCode() {
    return EXCEPTION_CODE;
  }
}
