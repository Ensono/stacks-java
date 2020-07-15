package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.QueryMenuController;
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

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * MenuControllerImpl - Menu Controller used to interact and manage menus API.
 */
@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

    Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

    private MenuService menuService;

    public QueryMenuControllerImpl(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public ResponseEntity<SearchMenuResult> searchMenu(final String searchTerm,
                                                       final UUID restaurantId,
                                                       final Integer pageSize,
                                                       final Integer pageNumber) {
        List<Menu> menuList;

        if (isNotEmpty(searchTerm) && nonNull(restaurantId)) {
            menuList = this.menuService.findAllByRestaurantIdAndNameContaining(
                    restaurantId, searchTerm, pageSize, pageNumber);
        } else if (isNotEmpty(searchTerm)) {
            menuList = this.menuService.findAllByNameContaining(searchTerm, pageSize, pageNumber);
        } else if (nonNull(restaurantId)) {
            menuList = this.menuService.findAllByRestaurantId(restaurantId, pageSize, pageNumber);
        } else {
            menuList = this.menuService.findAll(pageNumber, pageSize);
        }

        return ResponseEntity.ok(new SearchMenuResult(pageSize, pageNumber,
                menuList.stream().map(SearchMenuResultItem::new).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<Menu> getMenu(UUID id) {
        return ResponseEntity.of(this.menuService.findById(id));
    }
}
