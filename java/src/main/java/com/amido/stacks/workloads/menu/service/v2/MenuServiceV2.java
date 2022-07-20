package com.amido.stacks.workloads.menu.service.v2;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.CreateMenuMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.UpdateMenuMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.menu.service.data.MenuQueryService;
import com.amido.stacks.workloads.menu.service.v1.MenuService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceV2 extends MenuService {

  public MenuServiceV2(
      MenuRepository menuRepository,
      MenuQueryService menuQueryService,
      MenuMapper menuMapper,
      CreateMenuMapper createMenuMapper,
      UpdateMenuMapper updateMenuMapper,
      SearchMenuResultItemMapper searchMenuResultItemMapper) {
    super(
        menuRepository,
        menuQueryService,
        menuMapper,
        createMenuMapper,
        updateMenuMapper,
        searchMenuResultItemMapper);
  }

  @Override
  public MenuDTO get(UUID id) {

    return super.get(id);
  }
}
