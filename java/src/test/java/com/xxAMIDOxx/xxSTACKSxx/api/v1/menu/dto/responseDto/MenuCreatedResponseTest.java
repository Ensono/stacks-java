package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class MenuCreatedResponseTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(MenuCreatedResponse.class).verify();
  }
}