package com.xxAMIDOxx.xxSTACKSxx.model;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.CategoryBuilder.aCategory;
import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.aDefaultItem;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class CategoryTest {

  @Test
  public void testCategory() {
    // Given
    Category category =
        aCategory()
            .withDescription("1st Category Description")
            .withName("1st Category")
            .withItems(List.of(aDefaultItem()))
            .withId(UUID.randomUUID().toString())
            .build();

    // When // Then
    assertThat(category, TypeMatchers.matchesCategory(category));
  }
}
