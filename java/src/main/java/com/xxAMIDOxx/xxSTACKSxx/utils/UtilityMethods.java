package com.xxAMIDOxx.xxSTACKSxx.utils;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import org.springframework.data.domain.Sort;

/**
 * A class containing various utility methods.
 *
 * @author ArathyKrishna
 */
public class UtilityMethods {

    public static CosmosPageRequest pageRequestWithSort(Sort.Direction direction, String variable, int pageNo, int pageSize) {
        final Sort sort = Sort.by(direction, variable);
        return new CosmosPageRequest(pageNo, pageSize, null, sort);
    }
}
