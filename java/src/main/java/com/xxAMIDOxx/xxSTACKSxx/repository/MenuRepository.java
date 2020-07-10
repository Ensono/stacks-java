package com.xxAMIDOxx.xxSTACKSxx.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MenuRepository extends CosmosRepository<Menu, String> {
    /**
     *
     * @param restaurantId
     * @param pageable
     * @return
     */
    Page<Menu> findAllByRestaurantId(UUID restaurantId, Pageable pageable);

    /**
     * @param searchTerm
     * @param pageable
     * @return
     */
    Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable);

    /**
     * @param restaurantId
     * @param searchTerm
     * @param pageable
     * @return
     */
    Page<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Pageable pageable);

}
