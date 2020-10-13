package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import java.util.UUID;

public class ItemAlreadyExistsException extends MenuApiException {

  public ItemAlreadyExistsException(MenuCommand command, UUID categoryId, String name) {
    super(
        String.format(
            "An item with the name '%s' already exists for the category '%s' in menu with "
                + "id '%s'.",
            name, categoryId, command.getMenuId()),
        ExceptionCode.ITEM_ALREADY_EXISTS,
        command);
  }

  public ItemAlreadyExistsException(String itemName, String categoryId, String menuId, int operationCode, String correlationId) {
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

