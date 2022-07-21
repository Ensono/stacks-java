package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  @Getter private final MenuMapper menuMapper;

  private final SearchMenuResultItemMapper searchMenuResultItemMapper;

  public ResourceCreatedResponse create(@Valid CreateMenuRequest body, String correlationId) {

    return new ResourceCreatedResponse(UUID.randomUUID());
  }

  public SearchMenuResult search(
      String searchTerm, UUID restaurantId, Integer pageSize, Integer pageNumber) {

    List<Menu> menuList = new ArrayList<>();

    final String menuId = "d5785e28-306b-4e23-add0-3f9092d395f8";

    Menu mockMenu;
    if (restaurantId == null) {
      mockMenu =
          new Menu(
              menuId,
              "58a1df85-6bdc-412a-a118-0f0e394c1342",
              "name",
              "description",
              new ArrayList<>(),
              true);
    } else {
      mockMenu =
          new Menu(menuId, restaurantId.toString(), "name", "description", new ArrayList<>(), true);
    }

    menuList.add(mockMenu);

    return new SearchMenuResult(
        pageSize,
        pageNumber,
        menuList.stream().map(searchMenuResultItemMapper::toDto).collect(Collectors.toList()));
  }

  public MenuDTO get(UUID id, String correlationId) {

    final String restaurantId = "58a1df85-6bdc-412a-a118-0f0e394c1342";
    final String categoryId = "2c43dbda-7d4d-46fb-b246-bec2bd348ca1";
    final String itemId = "7e46a698-080b-45e6-a529-2c196d00791c";

    Menu menu =
        new Menu(id.toString(), restaurantId, "name", "description", new ArrayList<>(), true);
    Item item = new Item(itemId, "item name", "item description", 5.99d, true);
    Category category = new Category(categoryId, "cat name", "cat description", List.of(item));
    menu.addOrUpdateCategory(category);

    return menuMapper.toDto(menu);
  }

  public ResourceUpdatedResponse update(@Valid UpdateMenuRequest body, String correlationId) {
    return new ResourceUpdatedResponse(UUID.randomUUID());
  }
}
