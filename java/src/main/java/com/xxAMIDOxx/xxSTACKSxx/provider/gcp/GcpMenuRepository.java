package com.xxAMIDOxx.xxSTACKSxx.provider.gcp;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.GcpMenu;
import org.springframework.cloud.gcp.data.firestore.FirestoreReactiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface GcpMenuRepository extends FirestoreReactiveRepository<GcpMenu> {

  @Override
  Mono save(GcpMenu menu);

  /**
   * Query is constructed OOTB- out of the box, executed and results are fetched based param
   * restaurantId. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @return page of menu
   */
  Page<GcpMenu> findAllByRestaurantId(String restaurantId, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Page<GcpMenu> findAllByNameContaining(String searchTerm, Pageable pageable);

  /**
   * Query is constructed OOTB - out of the box, executed and results are fetched based param
   * restaurantId and searchTerm. Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @param searchTerm Menu name
   * @param pageable pagination
   * @return page of menu
   */
  Page<GcpMenu> findAllByRestaurantIdAndNameContaining(
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
  Page<GcpMenu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable);
}
