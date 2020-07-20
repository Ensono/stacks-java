package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;

public class MenuUpdatedEvent extends MenuEvent {

    public MenuUpdatedEvent(MenuCommand command) {
        super(command, EventCode.MENU_UPDATED);
    }
}
