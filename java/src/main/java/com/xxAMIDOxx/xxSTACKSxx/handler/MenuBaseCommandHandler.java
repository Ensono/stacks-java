package com.xxAMIDOxx.xxSTACKSxx.handler;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;

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
        Optional<Menu> menu = menuRepository.findById(command.getMenuId().toString());
        handleCommand(menu.get(), command);
        publishEvents(raiseApplicationEvents(menu.get(), command));
        return Optional.empty();
    }

    private void publishEvents(List<MenuEvent> menuEvents) {
        menuEvents.forEach(applicationEventPublisher::publish);
    }

    abstract Optional<UUID> handleCommand(Menu menu, T command);

    abstract List<MenuEvent> raiseApplicationEvents(Menu menu, T command);
}
