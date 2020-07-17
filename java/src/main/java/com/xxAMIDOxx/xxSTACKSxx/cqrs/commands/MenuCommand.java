package com.xxAMIDOxx.xxSTACKSxx.cqrs.commands;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.ApplicationCommand;

import java.util.UUID;

public class MenuCommand extends ApplicationCommand {
    private UUID menuId;
    public MenuCommand(OperationCode operationCode, String correlationId, UUID menuId) {
        super(operationCode.getCode(), correlationId);
        this.menuId = menuId;
    }

    public UUID getMenuId() {
        return menuId;
    }

    public void setMenuId(UUID menuId) {
        this.menuId = menuId;
    }
}
