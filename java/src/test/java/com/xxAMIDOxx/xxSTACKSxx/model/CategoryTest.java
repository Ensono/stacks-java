package com.xxAMIDOxx.xxSTACKSxx.model;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.matcher.TypeMatchers;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.builder.CategoryBuilder.aCategory;
import static com.xxAMIDOxx.xxSTACKSxx.builder.ItemBuilder.aDefaultItem;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
public class CategoryTest {

    @MockBean
    private MenuRepository menuRepository;

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