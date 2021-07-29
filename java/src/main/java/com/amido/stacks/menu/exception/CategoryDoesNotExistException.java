package com.amido.stacks.menu.exception;

public class CategoryDoesNotExistException extends MenuApiException {

  public CategoryDoesNotExistException(
      String categoryId, String menuId, int operationCode, String correlationId) {
    super(
        String.format(
            "A category with the id '%s' does not exist for menu with id '%s'.",
            categoryId, menuId),
        ExceptionCode.CATEGORY_DOES_NOT_EXIST,
        operationCode,
        correlationId);
  }
}
