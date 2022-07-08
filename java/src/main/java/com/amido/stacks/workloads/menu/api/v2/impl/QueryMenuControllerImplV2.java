package com.amido.stacks.workloads.menu.api.v2.impl;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v2.QueryMenuControllerV2;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryMenuControllerImplV2 implements QueryMenuControllerV2 {

  @Autowired private MenuMapper menuMapper;

  @Override
  public ResponseEntity<MenuDTO> getMenu(UUID id, String correlationId) {
    String restaurantId = "3930ddff-82ce-4f7e-b910-b0709b276cf0";

    Menu menu =
        new Menu(
            id.toString(), restaurantId, "0 Menu", "0 Menu Description", new ArrayList<>(), true);

    return ResponseEntity.ok(menuMapper.toDto(menu));
  }
}
