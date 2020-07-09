package com.xxAMIDOxx.xxSTACKSxx.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CosmosRepository<Menu, String> {
}
