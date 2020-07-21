package com.xxAMIDOxx.xxSTACKSxx.service;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import org.springframework.data.domain.Sort;

public class CosmosHelper {

    private CosmosHelper() {}

    public static CosmosPageRequest pageRequestWithSort(Sort.Direction direction,
                                                        String variable, int pageNo, int pageSize) {
        final Sort sort = Sort.by(direction, variable);
        return new CosmosPageRequest(pageNo, pageSize, null, sort);
    }
}
