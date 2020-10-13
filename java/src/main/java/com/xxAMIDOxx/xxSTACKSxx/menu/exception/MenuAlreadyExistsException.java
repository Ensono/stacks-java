package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

public class MenuAlreadyExistsException extends MenuApiException {

  public MenuAlreadyExistsException(String restaurantId, String name, int operationCode, String correlationId) {
    super(
        String.format(
            "A Menu with the name '%s' already exists for the restaurant with id '%s'.",
            name, restaurantId),
        ExceptionCode.MENU_ALREADY_EXISTS, operationCode, correlationId);
  }
}
