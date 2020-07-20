package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;

public class CreateMenuCommand extends MenuCommand {

    private String name;
    private String description;
    private UUID restaurantId;
    private Boolean enabled;

    public CreateMenuCommand(String correlationId, String name, String description, UUID restaurantId, Boolean enabled) {
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
