package com.xxAMIDOxx.xxSTACKSxx.menu.api.v2.impl;

import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v2.QueryMenuControllerV2;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.mappers.DomainToDtoMapper;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryMenuControllerImplV2 implements QueryMenuControllerV2 {

  private MenuQueryService menuQueryService;

  public QueryMenuControllerImplV2(MenuQueryService menuQueryService) {
    this.menuQueryService = menuQueryService;
  }

  @Override
  public ResponseEntity<MenuDTO> getMenu(UUID id, String correlationId) {
    Menu menu =
        this.menuQueryService
            .findById(id)
            .orElseThrow(
                () ->
                    new MenuNotFoundException(
                        id.toString(), OperationCode.GET_MENU_BY_ID.getCode(), correlationId));

    return ResponseEntity.ok(DomainToDtoMapper.toMenuDto(menu));
  }
}