package com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.command;

import com.xxAMIDOxx.xxSTACKSxx.core.events.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.CreateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.events.CategoryCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CreateCategoryHandler extends MenuBaseCommandHandler<CreateCategoryCommand> {

    private UUID categoryId;

    public CreateCategoryHandler(MenuRepository menuRepository, ApplicationEventPublisher applicationEventPublisher) {
        super(menuRepository, applicationEventPublisher);
    }

    @Override
    Optional<UUID> handleCommand(Menu menu, CreateCategoryCommand command) {
        menu.setCategories(addCategory(menu, command));
        menuRepository.save(menu);
        return Optional.of(categoryId);
    }

    @Override
    List<MenuEvent> raiseApplicationEvents(Menu menu, CreateCategoryCommand command) {
        return Arrays.asList(
                new MenuUpdatedEvent(command, UUID.fromString(menu.getId())),
                new CategoryCreatedEvent(command, UUID.fromString(menu.getId()), categoryId)
        );
    }

    List<Category> addCategory(Menu menu, CreateCategoryCommand command) {

        categoryId = UUID.randomUUID();
        List<Category> categories = menu.getCategories() == null ?
                new ArrayList<>()
                : menu.getCategories();

        if (categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
            throw new CategoryAlreadyExistsException(command.getCorrelationId(),
                    OperationCode.CREATE_CATEGORY,
                    UUID.fromString(menu.getId()),
                    command.getName());
        } else {
            categories.add(new Category(categoryId.toString(),
                    command.getName(),
                    command.getDescription(),
                    new ArrayList<>()));
            return categories;
        }
    }

}
