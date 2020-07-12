package com.xxAMIDOxx.xxSTACKSxx.repository.impl;

import com.microsoft.azure.spring.data.cosmosdb.core.CosmosTemplate;
import com.microsoft.azure.spring.data.cosmosdb.core.query.Criteria;
import com.microsoft.azure.spring.data.cosmosdb.core.query.CriteriaType;
import com.microsoft.azure.spring.data.cosmosdb.core.query.DocumentQuery;
import com.microsoft.azure.spring.data.cosmosdb.repository.support.CosmosEntityInformation;
import com.microsoft.azure.spring.data.cosmosdb.repository.support.SimpleCosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.UUID;

/**
 * MenuRepositoryServiceImpl class
 */
public class MenuRepositoryServiceImpl extends SimpleCosmosRepository<Menu, String> implements MenuRepository {

    @Autowired
    private CosmosTemplate cosmosTemplate;

    public MenuRepositoryServiceImpl(CosmosEntityInformation<Menu, String> metadata, ApplicationContext applicationContext) {
        super(metadata, applicationContext);
    }

    @Override
    public Page<Menu> findAllByRestaurantId(UUID restaurantId, Pageable pageable) {
        DocumentQuery query = new DocumentQuery(Criteria.getInstance(CriteriaType.IS_EQUAL, "restaurantId",
                Collections.singletonList(restaurantId))).with(pageable);
        return cosmosTemplate.paginationQuery(query, Menu.class, cosmosTemplate.getContainerName(Menu.class));
    }

    @Override
    public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {
        DocumentQuery query = new DocumentQuery(Criteria.getInstance(CriteriaType.CONTAINING, "name",
                Collections.singletonList(searchTerm))).with(pageable);
        return cosmosTemplate.paginationQuery(query, Menu.class, cosmosTemplate.getContainerName(Menu.class));
    }


    @Override
    public Page<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Pageable pageable) {
        return null;
    }
}
