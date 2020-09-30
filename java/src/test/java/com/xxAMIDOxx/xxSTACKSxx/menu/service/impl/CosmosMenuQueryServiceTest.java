package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenus;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.List;

import com.xxAMIDOxx.xxSTACKSxx.provider.azure.CosmosMenuQueryService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Tag("Unit")
public class CosmosMenuQueryServiceTest {

  @Test
  void findAll() {

    MenuRepositoryAdapter menuRepositoryAdapter = mock(MenuRepositoryAdapter.class);
    MenuQueryService menuQueryServiceImpl = new CosmosMenuQueryService(menuRepositoryAdapter);

    Pageable pageable = mock(Pageable.class);

    List<AzureMenu> results = createMenus(2);
    Page<AzureMenu> page1 = new PageImpl<>(results, pageable, 2);
    Page<AzureMenu> page2 = new PageImpl<>(results, pageable, 2);

    // Given
    given(menuRepositoryAdapter.findAll(any(Pageable.class))).willReturn(page1);
    given(menuRepositoryAdapter.findAll(eq(pageable))).willReturn(page2);

    // When
    List<AzureMenu> actualResults = menuQueryServiceImpl.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }
}
