package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.response.MenuDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class MenuDTOTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(MenuDTO.class).verify();
  }
}
