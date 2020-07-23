package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.cqrs.handler.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Handler for deleting menu
 *
 * @author ArathyKrishna
 */
@Component
public class DeleteMenuHandler implements CommandHandler<DeleteMenuCommand> {

    protected MenuRepository repository;

    private ApplicationEventPublisher publisher;

    public DeleteMenuHandler(MenuRepository repository,
                             ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public Optional<UUID> handle(DeleteMenuCommand command) {

        verifyMenuExists(command);

        repository.deleteById(command.getMenuId().toString());

        return Optional.empty();
    }

    protected void verifyMenuExists(DeleteMenuCommand command) {
        Optional<Menu> optMenu =
                repository.findById(command.getMenuId().toString());
        if (optMenu.isEmpty()) {
            throw new MenuNotFoundException(command);
        }
    }
}
