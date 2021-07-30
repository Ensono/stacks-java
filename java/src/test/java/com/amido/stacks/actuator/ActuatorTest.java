package com.amido.stacks.actuator;

import com.azure.spring.autoconfigure.cosmos.CosmosAutoConfiguration;
import com.azure.spring.autoconfigure.cosmos.CosmosRepositoriesAutoConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static com.amido.stacks.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
@EnableAutoConfiguration
@Tag("Component")
class ActuatorTest {

  @Value("${local.management.port}")
  private int mgt;

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void shouldReturn200WhenSendingRequestToHealthEndpoint() {

    var entity = this.testRestTemplate.getForEntity(getBaseURL(this.mgt) + "/health", Map.class);
    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void shouldReturn200WhenSendingRequestToManagementEndpoint() {

    var entity = this.testRestTemplate.getForEntity(getBaseURL(this.mgt) + "/info", Map.class);
    then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
