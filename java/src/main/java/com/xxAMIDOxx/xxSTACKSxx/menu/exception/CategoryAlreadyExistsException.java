package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;

public class CategoryAlreadyExistsException extends MenuApiException {

  public CategoryAlreadyExistsException(String name, String menuId, int operationCode, String correlationId) {
    super(
        String.format(
            "A category with the name '%s' already exists for the menu with id '%s'.",
            name, menuId),
        ExceptionCode.CATEGORY_ALREADY_EXISTS, operationCode, correlationId);
  }
}
