package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.QueryMenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.cqrs.handlers.query.QueryMenuHandler;
import com.xxAMIDOxx.xxSTACKSxx.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.mapper.MenuMapper;
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
 * MenuControllerImpl - MenuDTO Controller used to interact and manage menus API.
 */
@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

    Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

    private QueryMenuHandler queryMenuHandler;

    public QueryMenuControllerImpl(QueryMenuHandler queryMenuHandler) {
        this.queryMenuHandler = queryMenuHandler;
    }

    @Override
    public ResponseEntity<SearchMenuResult> searchMenu(final String searchTerm,
                                                       final UUID restaurantId,
                                                       final Integer pageSize,
                                                       final Integer pageNumber) {
        List<Menu> menuList;

        if (isNotEmpty(searchTerm) && nonNull(restaurantId)) {
            menuList = this.queryMenuHandler.findAllByRestaurantIdAndNameContaining(
                    restaurantId, searchTerm, pageSize, pageNumber);
        } else if (isNotEmpty(searchTerm)) {
            menuList = this.queryMenuHandler.findAllByNameContaining(searchTerm, pageSize, pageNumber);
        } else if (nonNull(restaurantId)) {
            menuList = this.queryMenuHandler.findAllByRestaurantId(restaurantId, pageSize, pageNumber);
        } else {
            menuList = this.queryMenuHandler.findAll(pageNumber, pageSize);
        }

        return ResponseEntity.ok(new SearchMenuResult(pageSize, pageNumber,
                menuList.stream().map(SearchMenuResultItem::new).collect(Collectors.toList())));
    }

    @Override
    public ResponseEntity<MenuDTO> getMenu(final UUID id, final String correlationId) {
        Menu menu = this.queryMenuHandler.findById(id).orElseThrow(
                () -> new MenuNotFoundException(
                        new MenuCommand(OperationCode.GET_MENU_BY_ID, correlationId, id)));
        return ResponseEntity.ok(MenuMapper.INSTANCE.menuToMenuDto(menu));
    }
}
