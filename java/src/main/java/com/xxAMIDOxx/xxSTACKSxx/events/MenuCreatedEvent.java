package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;

import java.util.UUID;

public class MenuCreatedEvent extends MenuEvent {

    public MenuCreatedEvent(MenuCommand command, UUID menuId) {
        super(command, EventCode.MENU_CREATED, menuId);
    }
}
