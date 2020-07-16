package com.xxAMIDOxx.xxSTACKSxx.cqrs.commands;

import java.util.UUID;

public class CreateMenuCommand extends MenuCommand {

    private String name = null;
    private String description = null;
    private UUID restaurantId = null;
    private Boolean enabled = null;

    public CreateMenuCommand(UUID correlationId, String name, String description, UUID restaurantId, Boolean enabled) {
        super(OperationCode.CREATE_MENU, correlationId, null);
        this.name = name;
        this.description = description;
        this.restaurantId = restaurantId;
        this.enabled = enabled;
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

    public UUID getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(UUID restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
