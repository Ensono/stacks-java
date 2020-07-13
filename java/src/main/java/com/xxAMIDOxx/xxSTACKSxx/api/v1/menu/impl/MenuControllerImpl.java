package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.MenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

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

        List<SearchMenuResultItem> menuResultItems;

        if (StringUtils.isNotEmpty(searchTerm)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, getSearchMenuBySearchTermAndPage(searchTerm, pageSize, pageNumber)));
        }

        if (Objects.nonNull(restaurantId)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, getSearchMenuByRestaurantIdAndPage(restaurantId, pageSize, pageNumber)));
        }

        List<Menu> menus = menuService.all(pageNumber, pageSize);
        menuResultItems = menus.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new SearchMenuResult(pageSize, pageNumber, menuResultItems));
    }

    private List<SearchMenuResultItem> getSearchMenuByRestaurantIdAndPage(UUID restaurantId, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "RestaurantId");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        Optional<Page<Menu>> pages = Optional.ofNullable(menuService.findAllByRestaurantId(restaurantId, pageRequest));
        return pages.map(menus -> menus.stream().map(SearchMenuResultItem::new)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }


    private List<SearchMenuResultItem> getSearchMenuBySearchTermAndPage(String searchTerm, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "Name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        Optional<Page<Menu>> pages = Optional.ofNullable(menuService.findAllByNameContaining(searchTerm, pageRequest));
        return pages.map(menus -> menus.stream().map(SearchMenuResultItem::new)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    @Override
    public ResponseEntity<Menu> getMenu(UUID id) {
        return ResponseEntity.of(this.menuService.findById(id));
    }
}
