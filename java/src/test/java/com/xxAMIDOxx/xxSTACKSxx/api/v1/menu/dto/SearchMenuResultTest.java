package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SearchMenuResultTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResult.class).verify();
  }
}
