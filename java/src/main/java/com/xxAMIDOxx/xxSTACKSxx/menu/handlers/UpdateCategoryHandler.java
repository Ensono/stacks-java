package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateCategoryCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ArathyKrishna
 */
@Component
public class UpdateCategoryHandler extends MenuBaseCommandHandler<UpdateCategoryCommand> {

    public UpdateCategoryHandler(MenuRepository repository,
                                 ApplicationEventPublisher publisher) {
        super(repository, publisher);
    }

    @Override
    Optional<UUID> handleCommand(Menu menu, UpdateCategoryCommand command) {
        menu.setCategories(updateCategory(menu, command));
        menuRepository.save(menu);
        return Optional.of(command.getCategoryId());
    }

    List<Category> updateCategory(Menu menu, UpdateCategoryCommand command) {

        Category category = getCategory(menu, command);

        if (menu.getCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
            throw new CategoryAlreadyExistsException(command, command.getName());
        } else {
            category.setDescription(command.getDescription());
            category.setName(command.getName());
        }
        return List.of(category);
    }

    @Override
    List<MenuEvent> raiseApplicationEvents(Menu menu,
                                           UpdateCategoryCommand command) {
        return Arrays.asList(new MenuUpdatedEvent(command),
                new CategoryUpdatedEvent(command, command.getCategoryId()));
    }

    Category getCategory(Menu menu,
                         UpdateCategoryCommand command) {

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
}
