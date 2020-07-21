package com.xxAMIDOxx.xxSTACKSxx.core.azure.cosmos;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class CosmosHelperTest {

  @Test
  void pageRequestWithSort() {

    // Given
    final Sort.Direction direction = Sort.Direction.ASC;
    final String attribute = "xyz";
    final int pageNumber = 99;
    final int pageSize = 98;

    // When
    CosmosPageRequest output = CosmosHelper.pageRequestWithSort(direction, attribute, pageNumber, pageSize);

    // Then
    assertEquals(direction, output.getSort().getOrderFor(attribute).getDirection());
    assertEquals(pageNumber, output.getPageNumber());
    assertEquals(pageSize, output.getPageSize());

  }
}
