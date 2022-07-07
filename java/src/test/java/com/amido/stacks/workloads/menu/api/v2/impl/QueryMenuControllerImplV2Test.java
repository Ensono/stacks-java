package com.amido.stacks.workloads.menu.api.v2.impl;

import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.workloads.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;

import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.mappers.CategoryMapper;
import com.amido.stacks.workloads.menu.mappers.CategoryMapperImpl;
import com.amido.stacks.workloads.menu.mappers.ItemMapper;
import com.amido.stacks.workloads.menu.mappers.ItemMapperImpl;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.MenuMapperImpl;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapperImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {
      MenuMapper.class,
      MenuMapperImpl.class,
      CategoryMapper.class,
      CategoryMapperImpl.class,
      ItemMapper.class,
      ItemMapperImpl.class,
      SearchMenuResultItemMapper.class,
      SearchMenuResultItemMapperImpl.class
    })
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class QueryMenuControllerImplV2Test {

  private final String GET_MENU_BY_ID = "%s/v2/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private MenuMapper menuMapper;

  @Test
  void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    String restaurantId = "3930ddff-82ce-4f7e-b910-b0709b276cf0";
    menu.setRestaurantId(restaurantId);
    MenuDTO expectedResponse = menuMapper.toDto(menu);

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), menu.getId()), MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }
}
