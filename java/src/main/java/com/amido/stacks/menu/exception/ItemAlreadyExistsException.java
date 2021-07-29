package com.amido.stacks.menu.exception;

import java.util.UUID;

public class ItemAlreadyExistsException extends MenuApiException {

  public ItemAlreadyExistsException(
      String itemName, String categoryId, UUID menuId, int operationCode, String correlationId) {
    super(
        String.format(
            "An item with the name '%s' already exists for the category '%s' in menu with "
                + "id '%s'.",
            itemName, categoryId, menuId),
        ExceptionCode.ITEM_ALREADY_EXISTS,
        operationCode,
        correlationId);
  }
}
