package com.xxAMIDOxx.xxSTACKSxx.model;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.CategoryBuilder.aCategory;
import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.aDefaultItem;
import static org.hamcrest.MatcherAssert.assertThat;

@Tag("Unit")
class CategoryTest {

  @Test
  void testCategory() {
    // Given
    Category category = aCategory()
            .withDescription("1st Category Description")
            .withName("1st Category")
            .withItems(List.of(aDefaultItem()))
            .withId(UUID.randomUUID().toString())
            .build();

    // When // Then
    assertThat(category, TypeMatchers.matchesCategory(category));
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(Category.class).verify();
  }

  @Test
  void testToString() {
    ToStringVerifier.forClass(Category.class)
            .withClassName(NameStyle.SIMPLE_NAME).verify();
  }
}
