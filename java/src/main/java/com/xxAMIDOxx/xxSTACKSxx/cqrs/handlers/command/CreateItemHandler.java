package com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command;

import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuItemCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CreateItemHandler extends MenuBaseCommandHandler<CreateItemCommand> {

    private UUID itemId;

    public CreateItemHandler(MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
        super(menuRepository, applicationEventPublisher);
    }

    @Override
    Optional<UUID> handleCommand(Menu menu, CreateItemCommand command) {
        itemId = UUID.randomUUID();
        Category category = addItem(getCategory(menu, command), command);
        menuRepository.save(menu.updateCategory(category));
        return Optional.of(itemId);
    }

    @Override
    List<MenuEvent> raiseApplicationEvents(Menu menu, CreateItemCommand command) {
        return Arrays.asList(
                new MenuUpdatedEvent(command),
                new CategoryUpdatedEvent(command, command.getCategoryId()),
                new MenuItemCreatedEvent(command, command.getCategoryId(), itemId)
        );
    }

    Category getCategory(Menu menu, CreateItemCommand command) {
        Optional<Category> existing = Optional.empty();

        if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
            existing = menu.getCategories()
                    .stream()
                    .filter(c -> c.getId().equals(command.getCategoryId().toString()))
                    .findFirst();
        }
        return existing.orElseThrow(() -> new CategoryDoesNotExistException(
                command,
                command.getCategoryId()));
    }

    Category addItem(Category category, CreateItemCommand command) {

        itemId = UUID.randomUUID();
        List<Item> items = category.getItems() == null ? new ArrayList<>()
                : category.getItems();

        if (items.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
            throw new ItemAlreadyExistsException(command,
                    command.getCategoryId(),
                    command.getName());
        } else {
            Item item = new Item(itemId.toString(),
                    command.getName(),
                    command.getDescription(),
                    command.getPrice(),
                    command.getAvailable());
            category.getItems().add(item);
            return category;
        }
    }

}
