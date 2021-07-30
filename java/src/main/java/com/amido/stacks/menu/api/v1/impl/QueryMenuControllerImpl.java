package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.QueryMenuController;
import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** MenuControllerImpl - MenuDTO Controller used to interact and manage menus API. */
@RestController
public class QueryMenuControllerImpl implements QueryMenuController {

  Logger logger = LoggerFactory.getLogger(QueryMenuControllerImpl.class);

  private DomainToDtoMapper mapper;

  public QueryMenuControllerImpl(DomainToDtoMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<SearchMenuResult> searchMenu(
      final String searchTerm,
      final UUID restaurantId,
      final Integer pageSize,
      final Integer pageNumber) {

    List<Menu> menuList = new ArrayList<>();

    final String menuId = "d5785e28-306b-4e23-add0-3f9092d395f8";

    Menu mockMenu;
    if(restaurantId == null){
      mockMenu = new Menu(menuId, "58a1df85-6bdc-412a-a118-0f0e394c1342", "name", "description", new ArrayList<>(), true);
    }
    else {
      mockMenu = new Menu(menuId, restaurantId.toString(), "name", "description", new ArrayList<>(), true);
    }

    menuList.add(mockMenu);

    return ResponseEntity.ok(
        new SearchMenuResult(
            pageSize,
            pageNumber,
            menuList.stream()
                .map(m -> mapper.toSearchMenuResultItem(m))
                .collect(Collectors.toList())));
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(final UUID id, final String correlationId) {
    final String restaurantId = "58a1df85-6bdc-412a-a118-0f0e394c1342";
    final String categoryId = "2c43dbda-7d4d-46fb-b246-bec2bd348ca1";
    final String itemId = "7e46a698-080b-45e6-a529-2c196d00791c";

    Menu menu = new Menu(id.toString(), restaurantId, "name", "description", new ArrayList<>(), true);
    Item item = new Item(itemId, "item name", "item description", 5.99d, true);
    Category category = new Category(categoryId, "cat name", "cat description", Arrays.asList(item));
    menu.addOrUpdateCategory(category);

    return ResponseEntity.ok(mapper.toMenuDto(menu));
  }
}
