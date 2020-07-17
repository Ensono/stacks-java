package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.SearchMenuResult;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class SearchMenuResultTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResult.class).verify();
  }
}
