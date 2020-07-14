package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.MenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.Objects;

/**
 * MenuControllerImpl - Menu Controller used to interact and manage menus API for a restaurant.
 */
@RestController
public class MenuControllerImpl implements MenuController {

    Logger logger = LoggerFactory.getLogger(MenuControllerImpl.class);

    private MenuService menuService;

    public MenuControllerImpl(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public ResponseEntity<SearchMenuResult> searchMenu(final String searchTerm,
                                                       final UUID restaurantId,
                                                       final Integer pageSize,
                                                       final Integer pageNumber) {

        if (StringUtils.isNotEmpty(searchTerm) && Objects.nonNull(restaurantId)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, this.menuService.findAllByRestaurantIdAndNameContaining(restaurantId, searchTerm, pageSize, pageNumber)));
        }

        if (StringUtils.isNotEmpty(searchTerm)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, this.menuService.findAllByNameContaining(searchTerm, pageSize, pageNumber)));
        }

        if (Objects.nonNull(restaurantId)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, this.menuService.findAllByRestaurantId(restaurantId, pageSize, pageNumber)));
        }

        return ResponseEntity.ok(
                new SearchMenuResult(pageSize, pageNumber, this.menuService.all(pageNumber, pageSize)));
    }


    @Override
    public ResponseEntity<Menu> getMenu(UUID id) {
        return ResponseEntity.of(this.menuService.findById(id));
    }
}