package com.xxAMIDOxx.xxSTACKSxx.model;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.anItem;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("Unit")
class ItemTest {

  @Test
  void testItem() {
    // Given
    Item item = anItem()
            .withAvailable(true)
            .withDescription("Some Description")
            .withName("1st Item")
            .withPrice(12.34d)
            .withId(UUID.randomUUID().toString())
            .build();

    // When // Then
    assertThat(item, TypeMatchers.matchesItem(item));
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(Item.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Item.class)
            .withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
