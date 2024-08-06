package com.amido.stacks.workloads.menu.service.v2;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.service.utility.MenuHelperService;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceV2 extends MenuService {

  public MenuServiceV2(
      MenuMapper menuMapper,
      SearchMenuResultItemMapper searchMenuResultItemMapper,
      MenuHelperService menuHelperService) {
    super(menuMapper, searchMenuResultItemMapper, menuHelperService);
  }

  @Override
  public MenuDTO get(UUID id, String correlationId) {

    String restaurantId = "3930ddff-82ce-4f7e-b910-b0709b276cf0";

    Menu menu =
        new Menu(
            id.toString(), restaurantId, "0 Menu", "0 Menu Description", new ArrayList<>(), true);

    return getMenuMapper().toDto(menu);
  }
}
