package com.xxAMIDOxx.xxSTACKSxx.model;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.anItem;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class ItemTest {

  @Test
  public void testItem() {
    // Given
    Item item =
        anItem()
            .withAvailable(true)
            .withDescription("Some Description")
            .withName("1st Item")
            .withPrice(12.34d)
            .withId(UUID.randomUUID().toString())
            .build();

    // When // Then
    assertThat(item, TypeMatchers.matchesItem(item));
  }
}
