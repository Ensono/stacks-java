package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class SearchMenuResultItemTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResultItem.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(SearchMenuResultItem.class)
            .withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
