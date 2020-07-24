package com.xxAMIDOxx.xxSTACKSxx.menu.events;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;

import java.util.UUID;

/**
 * Category update event class
 * @author ArathyKrishna
 */
public class CategoryUpdateEvent extends CategoryEvent {

    public CategoryUpdateEvent(MenuCommand menuCommand,
            EventCode eventCode, UUID categoryId) {
        super(menuCommand, EventCode.CATEGORY_UPDATED, categoryId);
    }
}
