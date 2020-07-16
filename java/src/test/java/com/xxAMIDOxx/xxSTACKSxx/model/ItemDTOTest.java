package com.xxAMIDOxx.xxSTACKSxx.model;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.ItemDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class ItemDTOTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(ItemDTO.class).verify();
  }
}
