package com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command;

import com.xxAMIDOxx.xxSTACKSxx.core.commands.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.exception.MenuAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        verifyMenuNotAlreadyExisting(command);

        final UUID id = UUID.randomUUID();
        final Menu menu = new Menu(id.toString(),
                command.getRestaurantId().toString(),
                command.getName(),
                command.getDescription(),
                new ArrayList<>(),
                command.getEnabled());

        menuRepository.save(menu);

        applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

        return Optional.of(id);
    }

    protected void verifyMenuNotAlreadyExisting(CreateMenuCommand command) {
        Page<Menu> existing = menuRepository.findAllByRestaurantIdAndNameContaining(
                command.getRestaurantId().toString(), command.getName(),
                Pageable.unpaged()
        );
        if (!existing.getContent().isEmpty() &&
                existing.get().anyMatch(m -> m.getName().equals(command.getName()))) {
            throw new MenuAlreadyExistsException(command,
                    command.getRestaurantId(),
                    command.getName());
        }
    }
}
