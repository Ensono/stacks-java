package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.SearchMenuResultItem;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
class SearchMenuResultItemTest {

  @Test
  void testEquals() {
    EqualsVerifier.simple().forClass(SearchMenuResultItem.class).verify();
  }
}
