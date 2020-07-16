package com.xxAMIDOxx.xxSTACKSxx.handler;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {

    protected MenuRepository menuRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    public CreateMenuHandler(MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.menuRepository = menuRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Optional<UUID> handle(CreateMenuCommand command) {

        final UUID id = UUID.randomUUID();

        Menu menu = new Menu(id.toString(),
                command.getRestaurantId().toString(),
                command.getName(),
                command.getDescription(),
                new ArrayList<>(),
                command.getEnabled());

        menuRepository.save(menu);

        applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

        return Optional.of(id);
    }
}
