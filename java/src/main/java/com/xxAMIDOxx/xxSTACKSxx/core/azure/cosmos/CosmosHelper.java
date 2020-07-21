package com.xxAMIDOxx.xxSTACKSxx.core.azure.cosmos;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import org.springframework.data.domain.Sort;

public class CosmosHelper {

    public static CosmosPageRequest pageRequestWithSort(Sort.Direction direction,
                                                        String attribute,
                                                        int pageNumber,
                                                        int pageSize) {
        final Sort sort = Sort.by(direction, attribute);
        return new CosmosPageRequest(pageNumber, pageSize, null, sort);
    }
}
