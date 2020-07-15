package com.xxAMIDOxx.xxSTACKSxx.core.commands;

import com.xxAMIDOxx.xxSTACKSxx.core.operations.OperationContext;

import java.util.UUID;

public abstract class ApplicationCommand extends OperationContext {
    public ApplicationCommand(int operationCode, UUID correlationId) {
        super(operationCode, correlationId);
    }
}
