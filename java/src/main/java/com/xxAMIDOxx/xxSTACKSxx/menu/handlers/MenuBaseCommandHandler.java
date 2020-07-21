package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class MenuBaseCommandHandler<T extends MenuCommand> implements CommandHandler<T> {

    protected MenuRepository menuRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    public MenuBaseCommandHandler(MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.menuRepository = menuRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Optional<UUID> handle(T command) {

        Menu menu = menuRepository.findById(command.getMenuId().toString())
                .orElseThrow(
                        () -> new MenuNotFoundException(command));

        // TODO: Check if the user owns the resource before any operation

        var result = handleCommand(menu, command);

        publishEvents(raiseApplicationEvents(menu, command));

        return result;
    }

    private void publishEvents(List<MenuEvent> menuEvents) {
        menuEvents.forEach(applicationEventPublisher::publish);
    }

    abstract Optional<UUID> handleCommand(Menu menu, T command);

    abstract List<MenuEvent> raiseApplicationEvents(Menu menu, T command);
}
