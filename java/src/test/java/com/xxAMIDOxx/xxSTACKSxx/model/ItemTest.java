package com.xxAMIDOxx.xxSTACKSxx.model;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.anItem;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
public class ItemTest {

    @MockBean
    private MenuRepository menuRepository;

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