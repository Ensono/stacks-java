package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author ArathyKrishna
 */
@Tag("Unit")
class MenuUpdateResponseDtoTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(MenuUpdateResponseDto.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(MenuUpdateResponseDto.class).verify();
  }

}