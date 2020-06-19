package com.xxAMIDOxx.xxSTACKSxx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ActuatorTest {

    @Value("${local.management.port}")
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String getBaseURL(final int port) {
        return "http://localhost:" + port;
    }

    @Test
    public void shouldReturn200WhenSendingRequestToHealthEndpoint() {

        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/actuator/health", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() {

        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/actuator/info", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
