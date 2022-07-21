package com.amido.stacks.workloads.menu.service.v1;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateItemRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.ItemDTO;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Item;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.ItemMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.CreateItemMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.UpdateItemMapper;
import com.amido.stacks.workloads.menu.service.data.MenuQueryService;
import com.amido.stacks.workloads.menu.service.v1.utility.MenuHelperService;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final MenuQueryService menuQueryService;

  private final MenuHelperService menuHelperService;

  private final CreateItemMapper createItemMapper;

  private final UpdateItemMapper updateItemMapper;

  private final ItemMapper itemMapper;

  public ResourceCreatedResponse create(
      UUID menuId, UUID categoryId, @Valid CreateItemRequest body, String correlationId) {

    Menu menu = getMenu(menuId);

    Category category = menuHelperService.checkCategoryExistsById(menu, categoryId);

    UUID itemId = UUID.randomUUID();

    menuHelperService.verifyItemNameNotAlreadyExisting(menuId, category, itemId, body.getName());

    ItemDTO itemDTO = createItemMapper.fromDto(body);
    itemDTO.setId(itemId.toString());

    menuHelperService.addOrUpdateItem(category, itemMapper.fromDto(itemDTO));
    menuHelperService.save(menu);

    return new ResourceCreatedResponse(itemId);
  }

  public ResourceUpdatedResponse update(
      UUID menuId,
      UUID categoryId,
      UUID itemId,
      @Valid UpdateItemRequest body,
      String correlationId) {

    Menu menu = getMenu(menuId);

    Category category = menuHelperService.checkCategoryExistsById(menu, categoryId);

    Item item = menuHelperService.checkItemExistsById(menuId, category, itemId);

    menuHelperService.verifyItemNameNotAlreadyExisting(menuId, category, itemId, body.getName());

    ItemDTO itemDTO = updateItemMapper.fromDto(body);
    itemDTO.setId(item.getId());

    menuHelperService.addOrUpdateItem(category, itemMapper.fromDto(itemDTO));
    menuHelperService.save(menu);

    return new ResourceUpdatedResponse(itemId);
  }

  public void delete(UUID menuId, UUID categoryId, UUID itemId, String correlationId) {

    Menu menu = getMenu(menuId);

    Category category = menuHelperService.checkCategoryExistsById(menu, categoryId);

    menuHelperService.checkItemExistsById(menuId, category, itemId);

    menuHelperService.removeItem(category, itemId);
    menuHelperService.save(menu);
  }

  private Menu getMenu(UUID menuId) {
    Optional<Menu> optMenu = menuQueryService.findById(menuId);

    if (optMenu.isEmpty()) {
      throw new MenuNotFoundException(menuId);
    }

    return optMenu.get();
  }
}
