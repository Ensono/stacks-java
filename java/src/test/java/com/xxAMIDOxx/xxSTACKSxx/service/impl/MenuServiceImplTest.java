package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
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
import java.util.Optional;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.model.MenuHelper.createMenus;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Unit")
class MenuServiceImplTest {

  @Mock
  MenuRepository repository;

  @Mock
  Menu menu;

  @Mock
  Category category;

  @InjectMocks
  MenuServiceImpl menuServiceImpl;

  @BeforeEach
  void init_mocks() {
    MockitoAnnotations.initMocks(this);
    menu = new Menu();
    menu.setEnabled(true);
    menu.setName("testMenu");
    menu.setRestaurantId(UUID.randomUUID());
    menu.setDescription("something");

    category = new Category();
    category.setId(UUID.randomUUID().toString());
    category.setName("test Category");
    category.setDescription("test Category Description");
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
  void testFindMenuById() {
    // Given
    when(repository.save(any(Menu.class))).thenReturn(menu);
    when(repository.findById(any(String.class))).thenReturn(Optional.of(menu));

    // When
    Menu actualResults = menuServiceImpl.saveMenu(menu);

    // Then
    Optional<Menu> byId =
            menuServiceImpl.findById(UUID.fromString(actualResults.getId()));
    Menu retrieved = byId.get();
    then(retrieved.getId()).isEqualTo(actualResults.getId());
    then(retrieved.getDescription()).isEqualTo(actualResults.getDescription());
    then(retrieved.getEnabled()).isTrue();
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

  @Test
  void testAddCategory() {
    // Given
    when(repository.save(any(Menu.class))).thenReturn(menu);
    when(repository.findById(any(String.class))).thenReturn(Optional.of(menu));
    Menu savedMenu = menuServiceImpl.saveMenu(menu);

    // When
    Category actualResult =
            menuServiceImpl.saveCategory(UUID.fromString(savedMenu.getId()), category);

    // Then
    then(actualResult).isSameAs(category);
    then(menu.getCategories()).hasSize(1);
    then(actualResult.getId()).isNotEmpty();
  }

  @Test
  void testCannotAddCategoryIfMenuDoesNotExist() {
    // Given
    when(repository.save(any(Menu.class))).thenReturn(new Menu());
    when(repository.findById(any(String.class))).thenReturn(Optional.of(new Menu()));

    // When
    Category actualResult =
            menuServiceImpl.saveCategory(UUID.randomUUID(), category);

    // Then
    then(actualResult).isNull();
  }
}
