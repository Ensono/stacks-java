package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenus;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Tag("Unit")
class MenuServiceTest {

  private final MenuRepository repository = mock(MenuRepository.class);
  private final ApplicationEventPublisher publisher = mock(ApplicationEventPublisher.class);

  @Test
  void findAll() {
    // Given
    MenuService menuQueryServiceImpl = new MenuService(repository, publisher);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    given(repository.findAll(any(Pageable.class))).willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryServiceImpl.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void testFindMenu() {
    // Given
    Menu menu = createMenu(1);
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    MenuService service = new MenuService(repository, publisher);
    Menu actual = service.findMenuOrThrowException(fromString(menu.getId()), 1, "");

    // Then
    then(actual.getId()).isEqualTo(menu.getId());
  }

  @Test
  void testFindMenuWithInvalidID() {
    // Given
    Menu menu = createMenu(1);
    when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    MenuService service = new MenuService(repository, publisher);

    // Then
    MenuNotFoundException menuNotFoundException =
        assertThrows(
            MenuNotFoundException.class,
            () -> service.findMenuOrThrowException(randomUUID(), 1, ""));
    then(menuNotFoundException.getMessage()).contains("A menu with id ");
  }

  @Test
  void testMenuCreatedEvents() {
    // Given a Menu creation request is received

    // When the Menu is created
    MenuService service = new MenuService(repository, publisher);
    List<MenuEvent> menuCreatedEvents =
        service.menuCreatedEvents(OperationCode.CREATE_MENU.getCode(), "", randomUUID());

    // Then
    then(menuCreatedEvents).hasSize(1);
  }

  @Test
  void testMenuUpdatedEvents() {
    // Given a Menu update request is received

    // When the Menu is updated
    MenuService service = new MenuService(repository, publisher);
    List<MenuEvent> menuEvents =
        service.menuUpdatedEvents(OperationCode.UPDATE_MENU.getCode(), "", randomUUID());

    // Then
    then(menuEvents).hasSize(1);
  }

  @Test
  void testMenuDeletedEvents() {
    // Given a Menu Delete request is received

    // When the Menu is deleted
    MenuService service = new MenuService(repository, publisher);
    List<MenuEvent> menuEvents =
        service.menuDeletedEvents(OperationCode.DELETE_MENU.getCode(), "", randomUUID());

    // Then
    then(menuEvents).hasSize(1);
  }
}
