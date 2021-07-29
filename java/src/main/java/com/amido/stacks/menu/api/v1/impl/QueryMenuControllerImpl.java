package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.controller.QueryMenuController;
import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

/** MenuControllerImpl - MenuDTO Controller used to interact and manage menus API. */
@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

  Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

  public QueryMenuControllerImpl() {
  }

  @Override
  public ResponseEntity<SearchMenuResult> searchMenu(
      final String searchTerm,
      final UUID restaurantId,
      final Integer pageSize,
      final Integer pageNumber) {
    List<Menu> menuList = new ArrayList<>();

    if(null == restaurantId){
      menuList.add(new Menu(randomUUID().toString(), randomUUID().toString(), "Menu 1", null, new ArrayList<>(), true));
      menuList.add(new Menu(randomUUID().toString(), randomUUID().toString(), "Menu 2", null, new ArrayList<>(), true));
    }
    else {
      menuList.add(new Menu(randomUUID().toString(), restaurantId.toString(), "Menu 1", null, new ArrayList<>(), true));
      menuList.add(new Menu(randomUUID().toString(), restaurantId.toString(), "Menu 2", null, new ArrayList<>(), true));
    }

    return ResponseEntity.ok(
        new SearchMenuResult(
            pageSize,
            pageNumber,
            menuList.stream()
                .map(DomainToDtoMapper::toSearchMenuResultItem)
                .collect(Collectors.toList())));
  }

  
  public ResponseEntity<MenuDTO> getMenu(final UUID id, final String correlationId) {
    Menu menu =new Menu(id.toString(), randomUUID().toString(), "Menu 3", null, new ArrayList<>(), true);

    return ResponseEntity.ok(DomainToDtoMapper.toMenuDto(menu));
  }
}
