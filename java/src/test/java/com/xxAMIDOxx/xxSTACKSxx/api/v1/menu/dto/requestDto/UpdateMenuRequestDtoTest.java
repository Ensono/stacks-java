package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author ArathyKrishna
 */
@Tag("Unit")
class UpdateMenuRequestDtoTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(UpdateMenuRequestDto.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(UpdateMenuRequestDto.class).verify();
  }

}