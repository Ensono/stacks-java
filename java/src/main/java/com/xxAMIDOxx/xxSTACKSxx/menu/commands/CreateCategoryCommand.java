package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;

public class CreateCategoryCommand extends MenuCommand {

    private String name;
    private String description;

    public CreateCategoryCommand(String correlationId, UUID menuId, String name, String description) {
        super(OperationCode.CREATE_CATEGORY, correlationId, menuId);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
