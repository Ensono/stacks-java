package com.amido.stacks.workloads.menu.service.data;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.domain.MenuHelper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Tag("Unit")
public class CosmosMenuQueryServiceTest {

  @Test
  void findById() {
    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Menu menu = MenuHelper.createMenu(1);

    given(repository.findById(anyString())).willReturn(Optional.of(menu));

    Optional<Menu> optMenu = menuQueryService.findById(UUID.randomUUID());

    then(optMenu).isNotEmpty();
    then(optMenu.get()).isEqualTo(menu);
  }

  @Test
  void findAll() {

    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = MenuHelper.createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    // Given
    given(repository.findAll(any(Pageable.class))).willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryService.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void findAllNextPage() {

    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = MenuHelper.createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    Page<Menu> mockPage1 = (Page<Menu>) mock(Page.class);

    // Given
    given(repository.findAll(any(Pageable.class))).willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryService.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void findAllByRestaurantId() {

    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = MenuHelper.createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    UUID restaurantId = UUID.randomUUID();

    // Given
    given(repository.findAllByRestaurantId(eq(restaurantId.toString()), any(Pageable.class)))
        .willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryService.findAllByRestaurantId(restaurantId, 2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void findAllByNameContaining() {

    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = MenuHelper.createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    String searchTerm = "SICB";

    // Given
    given(repository.findAllByNameContaining(eq(searchTerm), any(Pageable.class)))
        .willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryService.findAllByNameContaining(searchTerm, 2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  @Test
  void findAllByRestaurantIdAndNameContaining() {

    MenuRepository repository = mock(MenuRepository.class);
    MenuQueryService menuQueryService = new CosmosMenuQueryService(repository);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = MenuHelper.createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    String searchTerm = "SICB";
    UUID restaurantId = UUID.randomUUID();

    // Given
    given(
            repository.findAllByRestaurantIdAndNameContaining(
                eq(restaurantId.toString()), eq(searchTerm), any(Pageable.class)))
        .willReturn(page1);
    given(repository.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults =
        menuQueryService.findAllByRestaurantIdAndNameContaining(restaurantId, searchTerm, 2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }
}
