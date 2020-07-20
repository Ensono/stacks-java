package com.xxAMIDOxx.xxSTACKSxx.exception;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;

public class MenuApiException extends ApiException {

    public MenuApiException(String message,
                            ExceptionCode exceptionCode,
                            MenuCommand menuCommand) {
        super(message, exceptionCode,
                OperationCode.fromCode(menuCommand.getOperationCode()),
                menuCommand.getCorrelationId());
    }
}
