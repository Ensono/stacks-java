package com.amido.stacks.menu.api.v1.impl;

import com.amido.stacks.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.menu.api.v1.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.menu.domain.Menu;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static com.amido.stacks.menu.domain.MenuHelper.createMenu;
import static com.amido.stacks.util.TestHelper.getBaseURL;
import static com.amido.stacks.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;

;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("Integration")
@ActiveProfiles("test")
class UpdateMenuControllerImplTest {

  public static final String UPDATE_MENU = "%s/v1/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void testUpdateSuccess() {
    // Given
    Menu menu = createMenu(0);

    UpdateMenuRequest request = new UpdateMenuRequest("new name", "new description", false);

    // When
    var response =
        this.testRestTemplate.exchange(
            String.format(UPDATE_MENU, getBaseURL(port), menu.getId()),
            HttpMethod.PUT,
            new HttpEntity<>(request, getRequestHttpEntity()),
            ResourceUpdatedResponse.class);

    then(response).isNotNull();
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
