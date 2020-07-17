package com.xxAMIDOxx.xxSTACKSxx.exception;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

import java.util.UUID;

public class MenuNotFoundException extends ApiException {

    public MenuNotFoundException(String correlationId, OperationCode operationCode, UUID menuId) {
        super(String.format("A menu with id '%s' does not exist.", menuId),
                ExceptionCode.MENU_DOES_NOT_EXIST,
                operationCode,
                correlationId);
    }
}
