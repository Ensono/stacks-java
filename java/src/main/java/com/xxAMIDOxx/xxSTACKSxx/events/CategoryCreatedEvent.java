package com.xxAMIDOxx.xxSTACKSxx.events;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;

import java.util.UUID;

public class CategoryCreatedEvent extends CategoryEvent {

    public CategoryCreatedEvent(MenuCommand command, UUID categoryId) {
        super(command, EventCode.CATEGORY_CREATED, categoryId);
    }

}
