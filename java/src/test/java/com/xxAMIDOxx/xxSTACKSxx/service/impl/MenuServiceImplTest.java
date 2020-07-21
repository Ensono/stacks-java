package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.model.MenuHelper.createMenus;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Unit")
public class MenuServiceImplTest {

  @Mock
  MenuRepository repository;

  @Mock
  Menu menu;

  @InjectMocks
  MenuServiceImpl menuServiceImpl;

  @BeforeEach
  void init_mocks() {
    MockitoAnnotations.initMocks(this);
    menu = new Menu();
    menu.setId(UUID.randomUUID().toString());
    menu.setEnabled(true);
    menu.setName("testMenu");
    menu.setRestaurantId(UUID.randomUUID());
    menu.setDescription("something");
  }

  @Test
  void findAll() {

    // Given
    MenuRepository repository = mock(MenuRepository.class);
    MenuService menuServiceImpl = new MenuServiceImpl(repository);

    List<Menu> results = createMenus(2);
    Page<Menu> page1 = mock(Page.class);
    Page<Menu> page2 = new PageImpl<>(results);
    Pageable pageable = mock(Pageable.class);

    when(repository.findAll(any(Pageable.class))).thenReturn(page1);
    when(page1.hasNext()).thenReturn(true);
    when(page1.nextPageable()).thenReturn(pageable);
    when(repository.findAll(eq(pageable))).thenReturn(page2);

    // When
    List<Menu> actualResults = menuServiceImpl.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void testSaveMenu() {
    // Given
    when(repository.save(any(Menu.class))).thenReturn(menu);
    // When
    Menu actualResults = menuServiceImpl.saveMenu(menu);
    // Then
    then(actualResults).isSameAs(menu);
    then(actualResults.getId()).isNotEmpty();
  }
}
