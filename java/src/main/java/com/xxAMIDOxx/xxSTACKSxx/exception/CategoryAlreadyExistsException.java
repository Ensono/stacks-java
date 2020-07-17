package com.xxAMIDOxx.xxSTACKSxx.exception;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class CategoryAlreadyExistsException extends ApiException {

    public CategoryAlreadyExistsException(String correlationId, OperationCode operationCode, UUID menuId, String name) {
        super(String.format(
                "A category with the name '%s' already exists for the menu with id '%s'.", name, menuId),
                ExceptionCode.CATEGORY_ALREADY_EXISTS,
                operationCode,
                correlationId);
    }
}
