package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author ArathyKrishna
 */
@Tag("Unit")
class MenuCreateRequestDtoTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(MenuCreateRequestDto.class).verify();
  }

}