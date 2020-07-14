package com.xxAMIDOxx.xxSTACKSxx.utils;

import com.microsoft.azure.spring.data.cosmosdb.core.query.CosmosPageRequest;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class containing various utility methods.
 *
 * @author ArathyKrishna
 */
public class MenuResultUtils {

    public static CosmosPageRequest pageRequestWithSort(Sort.Direction direction, String variable, int pageNo, int pageSize) {
        final Sort sort = Sort.by(direction, variable);
        return new CosmosPageRequest(pageNo, pageSize, null, sort);
    }

    public static List<SearchMenuResultItem> getSearchMenuResultItems(
            Optional<List<Menu>> menusList) {

        return menusList.map(menus -> menus.stream()
                .map(SearchMenuResultItem::new)
                .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }
}
