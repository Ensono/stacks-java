// package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;
//
// import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
// import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getRequestHttpEntity;
// import static org.assertj.core.api.BDDAssertions.then;
// import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
//
// import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.request.GenerateTokenRequest;
// import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.GenerateTokenResponse;
// import org.junit.Ignore;
// import org.junit.jupiter.api.Tag;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.web.server.LocalServerPort;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
//
// @Ignore
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Tag("Integration")
// class AuthControllerImplTest {
//
//  public static final String GENERATE_TOKEN_URI = "%s/v1/token";
//
//  @LocalServerPort private int port;
//
//  @Autowired private TestRestTemplate testRestTemplate;
//
//  @Test
//  void testGenerateTokenWhenInvalidCredentials() {
//    // Given
//    GenerateTokenRequest requestBody = new GenerateTokenRequest();
//    requestBody.setClient_id("mock_Client_id");
//    requestBody.setClient_secret("mock_Client_secret");
//    requestBody.setAudience("https://mock.eu.auth0.com/api/v2/");
//    requestBody.setGrant_type("client_credentials");
//
//    // When
//    String requestUrl = String.format(GENERATE_TOKEN_URI, getBaseURL(port));
//    var response =
//        this.testRestTemplate.exchange(
//            requestUrl,
//            HttpMethod.POST,
//            new HttpEntity<>(requestBody, getRequestHttpEntity()),
//            GenerateTokenResponse.class);
//
//    // Then
//    then(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
//  }
// }
