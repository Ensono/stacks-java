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
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
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

        List<SearchMenuResultItem> menuResultItems;
        if (StringUtils.isNotEmpty(searchTerm) && Objects.nonNull(restaurantId) ) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, getResultsByRestaurantIdAndNameContaining(searchTerm,restaurantId, pageSize, pageNumber)));
        }

        if (StringUtils.isNotEmpty(searchTerm)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, getResultsByNameContaining(searchTerm, pageSize, pageNumber)));
        }

        if (Objects.nonNull(restaurantId)) {
            return ResponseEntity.ok(
                    new SearchMenuResult(pageSize, pageNumber, getAllResultsByRestaurantId(restaurantId, pageSize, pageNumber)));
        }

        List<Menu> menus = menuService.all(pageNumber, pageSize);
        menuResultItems = menus.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new SearchMenuResult(pageSize, pageNumber, menuResultItems));
    }

    private List<SearchMenuResultItem> getAllResultsByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "restaurantId");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        Optional<Page<Menu>> pages = Optional.ofNullable(this.menuService.findAllByRestaurantId(restaurantId, pageRequest));
        return getSearchMenuResultItems(pages);
    }


    private List<SearchMenuResultItem> getResultsByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        Optional<Page<Menu>> pages = Optional.ofNullable(this.menuService.findAllByNameContaining(searchTerm, pageRequest));
        return getSearchMenuResultItems(pages);
    }

    private List<SearchMenuResultItem> getResultsByRestaurantIdAndNameContaining(String searchTerm, UUID restaurantId , Integer pageSize, Integer pageNumber) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "name");
        final CosmosPageRequest pageRequest = new CosmosPageRequest(pageNumber, pageSize, null, sort);
        Optional<Page<Menu>> pages = Optional.ofNullable(this.menuService.findAllByRestaurantIdAndNameContaining(restaurantId,searchTerm, pageRequest));
        return getSearchMenuResultItems(pages);
    }

    private List<SearchMenuResultItem> getSearchMenuResultItems(Optional<Page<Menu>> pages) {
        return pages.map(menus -> menus.stream().map(SearchMenuResultItem::new)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
    }


    @Override
    public ResponseEntity<Menu> getMenu(UUID id) {
        return ResponseEntity.of(this.menuService.findById(id));
    }
}