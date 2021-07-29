package com.amido.stacks.menu.exception;

public class MenuNotFoundException extends MenuApiException {

  public MenuNotFoundException(String menuId, int operationCode, String correlationId) {
    super(
        String.format("A menu with id '%s' does not exist.", menuId),
        ExceptionCode.MENU_DOES_NOT_EXIST,
        operationCode,
        correlationId);
  }
}
