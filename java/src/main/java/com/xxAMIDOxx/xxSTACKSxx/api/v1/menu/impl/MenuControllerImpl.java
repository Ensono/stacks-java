package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.MenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class MenuControllerImpl implements MenuController {

    Logger logger = LoggerFactory.getLogger(MenuControllerImpl.class);

    private MenuService repository;

    public MenuControllerImpl(MenuService repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<SearchMenuResult> searchMenu(final String searchTerm,
                                                       final UUID restaurantId,
                                                       final Integer pageSize,
                                                       final Integer pageNumber) {

        List<Menu> menus = repository.all(pageNumber, pageSize);
        List<SearchMenuResultItem> menuResultItems = menus.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new SearchMenuResult(pageSize, pageNumber, menuResultItems));
    }

    @Override
    public ResponseEntity<Menu> getMenu(UUID id) {
        return ResponseEntity.of(this.repository.findById(id));
    }
}

