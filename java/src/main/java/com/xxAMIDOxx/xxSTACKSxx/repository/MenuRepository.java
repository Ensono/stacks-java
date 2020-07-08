package com.xxAMIDOxx.xxSTACKSxx.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(collectionResourceRel = "menu", path = "menu")
public interface MenuRepository extends CosmosRepository<Menu, String> {
}
