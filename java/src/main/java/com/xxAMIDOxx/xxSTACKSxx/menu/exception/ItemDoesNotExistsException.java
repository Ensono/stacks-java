package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import java.util.UUID;

public class ItemDoesNotExistsException extends MenuApiException {

  public ItemDoesNotExistsException(UUID categoryId, UUID itemId, UUID menuId, int operationCode, String correlationId) {
    super(
            String.format(
                    "An item with the id '%s' does not exists for category with the id '%s' and for menu with id '%s'.",
                    itemId, categoryId, menuId),
            ExceptionCode.ITEM_DOES_NOT_EXIST,
            operationCode,
            correlationId);
  }
}
