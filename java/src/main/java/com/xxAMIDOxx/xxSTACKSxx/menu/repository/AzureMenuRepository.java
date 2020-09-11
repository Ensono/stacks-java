package com.xxAMIDOxx.xxSTACKSxx.menu.repository;

import com.microsoft.azure.spring.data.cosmosdb.repository.CosmosRepository;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AzureMenuRepository extends CosmosRepository<Menu, String> {

  @Override
  Menu save(Menu menu);

  /**
   * Query is constructed OOTB- out of the box, executed and results are fetched based param
   * restaurantId. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @return page of menu
   */
  Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * restaurantId and searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Page<Menu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * restaurantId and searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId restaurant Id
   * @param name menu name
   * @param pageable paging requirements
   * @return
   */
  Page<Menu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable);
}
