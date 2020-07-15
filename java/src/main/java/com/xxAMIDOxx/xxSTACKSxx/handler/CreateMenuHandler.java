package com.xxAMIDOxx.xxSTACKSxx.handler;

import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateMenuHandler extends MenuBaseCommandHandler<CreateMenuCommand> {
    @Override
    Optional<UUID> handleCommand(Menu menu, CreateMenuCommand command) {
        return Optional.empty();
    }

    @Override
    List<MenuEvent> raiseApplicationEvents(Menu menu, CreateMenuCommand command) {
        return Collections.singletonList(
                new MenuCreatedEvent(command, UUID.fromString(menu.getId()))
        );
    }
}
