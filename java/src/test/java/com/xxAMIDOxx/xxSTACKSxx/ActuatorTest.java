package com.xxAMIDOxx.xxSTACKSxx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.utilities.HelperMethods.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author James Peet
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class ActuatorTest {

    @Value("${local.management.port}")
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn200WhenSendingRequestToHealthEndpoint() {

        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/health", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturn200WhenSendingRequestToManagementEndpoint() {

        var entity = this.testRestTemplate.getForEntity(
                getBaseURL(this.mgt) + "/info", Map.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
