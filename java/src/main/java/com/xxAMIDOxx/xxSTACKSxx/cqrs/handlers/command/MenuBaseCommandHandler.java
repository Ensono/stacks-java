package com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode.fromCode;

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
                        () -> new MenuNotFoundException(
                                command.getCorrelationId(),
                                fromCode(command.getOperationCode()),
                                command.getMenuId()));

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
