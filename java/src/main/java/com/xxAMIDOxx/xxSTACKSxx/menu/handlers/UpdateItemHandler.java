package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.UpdateItemCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.CategoryUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuItemUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.CategoryDoesNotExistException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.ItemDoesNotExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** @author ArathyKrishna */
@Component
public class UpdateItemHandler extends MenuBaseCommandHandler<UpdateItemCommand> {

  public UpdateItemHandler(MenuRepository repository, ApplicationEventPublisher publisher) {
    super(repository, publisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, UpdateItemCommand command) {
    menu.addUpdateCategory(updateItem(menu, command));
    menuRepository.save(menu);
    return Optional.of(command.getItemId());
  }

  Category updateItem(Menu menu, UpdateItemCommand command) {
    Category category = getCategory(menu, command);
    Item item = getItem(category, command);
    if (category.getItems().stream()
        .anyMatch(c -> c.getName().equalsIgnoreCase(command.getName()))) {
      throw new ItemAlreadyExistsException(command, command.getCategoryId(), command.getName());
    } else {
      item.setAvailable(command.getAvailable());
      item.setDescription(command.getDescription());
      item.setName(command.getName());
      item.setPrice(command.getPrice());
      category.addUpdateItem(item);
    }
    return category;
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, UpdateItemCommand command) {
    return Arrays.asList(
        new MenuItemUpdatedEvent(command, command.getCategoryId(), command.getItemId()),
        new CategoryUpdatedEvent(command, command.getCategoryId()),
        new MenuUpdatedEvent(command));
  }

  Category getCategory(Menu menu, UpdateItemCommand command) {
    Optional<Category> existing = Optional.empty();

    if (menu.getCategories() != null && !menu.getCategories().isEmpty()) {
      existing =
          menu.getCategories().stream()
              .filter(c -> c.getId().equals(command.getCategoryId().toString()))
              .findFirst();
    }
    return existing.orElseThrow(
        () -> new CategoryDoesNotExistException(command, command.getCategoryId()));
  }

  Item getItem(Category category, UpdateItemCommand command) {

    Optional<Item> existing = Optional.empty();
    if (category.getItems() != null && !category.getItems().isEmpty()) {
      existing =
          category.getItems().stream()
              .filter(t -> t.getId().equals(command.getItemId().toString()))
              .findFirst();
    }
    return existing.orElseThrow(
        () ->
            new ItemDoesNotExistsException(command, command.getCategoryId(), command.getItemId()));
  }
}
