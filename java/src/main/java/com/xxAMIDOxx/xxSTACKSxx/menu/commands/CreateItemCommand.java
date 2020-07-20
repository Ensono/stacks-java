package com.xxAMIDOxx.xxSTACKSxx.menu.commands;

import java.util.UUID;

public class CreateItemCommand extends MenuCommand {

    private UUID categoryId;
    private String name;
    private String description;
    private Double price = null;
    private Boolean available = null;

    public CreateItemCommand(String correlationId, UUID menuId,
                             UUID categoryId, String name,
                             String description, Double price, Boolean available) {
        super(OperationCode.CREATE_MENU_ITEM, correlationId, menuId);
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
