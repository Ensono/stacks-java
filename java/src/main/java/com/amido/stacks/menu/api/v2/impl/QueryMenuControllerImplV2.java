package com.amido.stacks.menu.api.v2.impl;

import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v2.QueryMenuControllerV2;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class QueryMenuControllerImplV2 implements QueryMenuControllerV2 {

  public QueryMenuControllerImplV2() {
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(UUID id, String correlationId) {
    Menu menu =new Menu(id.toString(), UUID.randomUUID().toString(), "Menu 3", null, new ArrayList<>(), true);

    return ResponseEntity.ok(DomainToDtoMapper.toMenuDto(menu));
  }
}
