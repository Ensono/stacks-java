package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * @author ArathyKrishna
 */
@Tag("Unit")
class CreateCategoryRequestDtoTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(CreateCategoryRequestDto.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(CreateCategoryRequestDto.class)
            .withClassName(NameStyle.SIMPLE_NAME).verify();
  }

}