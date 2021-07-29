package com.amido.stacks.menu.api.v2.impl;

import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("Integration")
@ActiveProfiles("test")
class QueryMenuControllerImplV2Test {

  private final String GET_MENU_BY_ID = "%s/v2/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    MenuDTO expectedResponse = DomainToDtoMapper.toMenuDto(menu);

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), menu.getId()), MenuDTO.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(OK);
  }
}
