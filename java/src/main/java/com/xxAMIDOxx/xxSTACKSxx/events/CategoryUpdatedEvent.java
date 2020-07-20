package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;

import java.util.UUID;

public class CategoryUpdatedEvent extends CategoryEvent {

    public CategoryUpdatedEvent(MenuCommand command, UUID categoryId) {
        super(command, EventCode.CATEGORY_UPDATED, categoryId);
    }

}
