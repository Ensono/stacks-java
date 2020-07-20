package com.xxAMIDOxx.xxSTACKSxx.menu.handlers.query.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.handlers.query.QueryMenuHandler;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenus;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@Tag("Unit")
public class QueryMenuHandlerTest {

    @Test
    void findAll() {

        MenuRepository repository = mock(MenuRepository.class);
        QueryMenuHandler queryMenuHandlerImpl = new CosmosQueryMenuHandler(repository);

        Pageable pageable = mock(Pageable.class);

        List<Menu> results = createMenus(2);
        Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
        Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

        // Given
        given(repository.findAll(any(Pageable.class))).willReturn(page1);
        given(repository.findAll(eq(pageable))).willReturn(page2);

        // When
        List<Menu> actualResults = queryMenuHandlerImpl.findAll(2, 5);

        // Then
        then(actualResults).isEqualTo(results);
    }
}
