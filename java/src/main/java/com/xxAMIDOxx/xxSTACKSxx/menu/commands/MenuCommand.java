package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import com.xxAMIDOxx.xxSTACKSxx.core.cqrs.command.ApplicationCommand;

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
