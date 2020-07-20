package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;

import java.util.UUID;

public class MenuItemCreatedEvent extends MenuItemEvent {
    public MenuItemCreatedEvent(MenuCommand command, UUID categoryId, UUID itemId) {
        super(command, EventCode.MENU_ITEM_CREATED, categoryId, itemId);
    }
}
