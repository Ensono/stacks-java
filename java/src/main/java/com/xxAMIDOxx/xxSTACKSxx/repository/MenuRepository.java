package com.xxAMIDOxx.xxSTACKSxx.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface MenuRepository extends CosmosRepository<Menu, String> {
    /**
     * Query is constructed OOTB- out of the box, executed and results are fetched based param restaurantId.
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId tenantID/RestaurantId
     * @return page of menu
     */
    // TODO throw exception and send that to the Service
    Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable);

    /**
     * Query is constructed OOTB - out of the box, executed and results are fetched based param searchTerm.
     * Pagination and sorting is done by spring data JPA.
     *
     * @param searchTerm Menu name
     * @param pageable pagination
     * @return page of menu
     */
    Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable);

    /**
     * Query is constructed OOTB - out of the box, executed and results are fetched based param restaurantId and searchTerm.
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId tenantID/RestaurantId
     * @param searchTerm Menu name
     * @param pageable pagination
     * @return page of menu
     */
    Page<Menu> findAllByRestaurantIdAndNameContaining(String restaurantId, String searchTerm, Pageable pageable);

}