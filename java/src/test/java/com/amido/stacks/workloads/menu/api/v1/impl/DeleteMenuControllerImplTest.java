package com.amido.stacks.workloads.menu.api.v1.impl;

import static com.amido.stacks.workloads.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.workloads.util.TestHelper.getBaseURL;
import static com.amido.stacks.workloads.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpStatus.OK;

import com.amido.stacks.workloads.menu.domain.Menu;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
@Tag("Integration")
@ActiveProfiles("test")
class DeleteMenuControllerImplTest {

  public static final String DELETE_MENU = "%s/v1/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testDeleteMenuSuccess() {
    // Given
    Menu menu = createMenu(1);

    var response =
        this.testRestTemplate.exchange(
            String.format(DELETE_MENU, getBaseURL(port), menu.getId()),
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ResponseEntity.class);
    // Then
    then(response.getStatusCode()).isEqualTo(OK);
  }
}
