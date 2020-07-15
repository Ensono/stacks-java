package com.xxAMIDOxx.xxSTACKSxx.cqrs.commands;

import java.util.UUID;

public class CreateMenuCommand extends MenuCommand {

    private String name = null;
    private String description = null;
    private UUID restaurantId = null;
    private Boolean enabled = null;

    public CreateMenuCommand(UUID correlationId, String name, String description, UUID restaurantId, Boolean enabled) {
        super(OperationCode.CREATE_MENU, correlationId);
        this.name = name;
        this.description = description;
        this.restaurantId = restaurantId;
        this.enabled = enabled;
    }

}
