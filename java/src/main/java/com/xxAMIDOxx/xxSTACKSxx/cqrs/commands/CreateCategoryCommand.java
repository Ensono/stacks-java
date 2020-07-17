package com.xxAMIDOxx.xxSTACKSxx.cqrs.commands;

public class CreateCategoryCommand extends MenuCommand {

    private String name;
    private String description;

    public CreateCategoryCommand(String correlationId, String name, String description) {
        super(OperationCode.CREATE_CATEGORY, correlationId, null);
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
