package com.xxAMIDOxx.xxSTACKSxx.core.commands;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

public abstract class ApplicationCommand extends OperationContext {
    public ApplicationCommand(int operationCode, String correlationId) {
        super(operationCode, correlationId);
    }
}
