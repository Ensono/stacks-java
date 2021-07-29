package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.menu.api.v1.dto.response.SearchMenuResultItem;
import com.amido.stacks.menu.domain.Category;
import com.amido.stacks.menu.domain.Item;
import com.amido.stacks.menu.domain.Menu;
import com.amido.stacks.menu.mappers.DomainToDtoMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.menu.domain.MenuHelper.createMenus;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("Integration")
@ActiveProfiles("test")
public class QueryMenuControllerImplTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  final int DEFAULT_PAGE_NUMBER = 1;
  final int DEFAULT_PAGE_SIZE = 20;

  @Test
  public void listMenusAndPagination() {

    // Given
    int pageNumber = 5;
    int pageSize = 6;

    // When
    var response =
        this.testRestTemplate.getForEntity(
            getBaseURL(port)
                + String.format("/v1/menu?pageSize=%d&pageNumber=%d", pageSize, pageNumber),
            SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void listMenusWithDefaultPagination() {

    // When
    var response =
        this.testRestTemplate.getForEntity(getBaseURL(port) + "/v1/menu", SearchMenuResult.class);

    // Then
    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(response.getBody()).isInstanceOf(SearchMenuResult.class);
    SearchMenuResult actual = response.getBody();
    assertThat(actual, is(notNullValue()));
    assertThat(actual.getPageNumber(), is(DEFAULT_PAGE_NUMBER));
    assertThat(actual.getPageSize(), is(DEFAULT_PAGE_SIZE));
  }
}
