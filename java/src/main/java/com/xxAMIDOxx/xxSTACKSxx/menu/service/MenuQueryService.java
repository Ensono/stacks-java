package com.xxAMIDOxx.xxSTACKSxx.menu.service;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuQueryService {

  /**
   * Retrieve list of all available Menus.
   *
   * @param pageNumber pageNo
   * @param pageSize page Size
   * @return List of MenuDTO
   */
  List<Menu> findAll(int pageNumber, int pageSize);

  /**
   * Retrieve MenuDTO by MenuDTO Id.
   *
   * @param id menu Id
   * @return Optional MenuDTO
   */
  Optional<Menu> findById(UUID id);

  /**
   * Retrieve MenuDTO by Restaurant Id Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId restaurant id
   * @param pageSize page size
   * @param pageNumber page no
   * @return List of MenuDTO
   */
  List<Menu> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber);

  /**
   * Retrieve MenuDTO's by matching the Restaurant Id and name. Pagination and sorting is done by
   * spring data JPA.
   *
   * @param searchTerm menu name
   * @param pageSize page size
   * @param pageNumber page no
   * @return List of MenuDTO
   */
  List<Menu> findAllByRestaurantIdAndName(
      UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber);

  /**
   * Retrieve MenuDTO's by matching the name (Contains operation) Pagination and sorting is done by
   * spring data JPA.
   *
   * @param searchTerm menu name
   * @param pageSize page size
   * @param pageNumber page no
   * @return List of MenuDTO
   */
  List<Menu> findAllByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber);

  /**
   * Retrieve MenuDTO's by matching the name and the restaurantId (Contains operation) Pagination
   * and sorting is done by spring data JPA.
   *
   * @param restaurantId restaurant id
   * @param searchTerm menu name
   * @param pageSize page size
   * @param pageNumber page no
   * @return List of MenuDTO
   */
  List<Menu> findAllByRestaurantIdAndNameContaining(
      UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber);

  /**
   * Create menu.
   *
   * @param menu Menu to update
   */
  UUID create(Menu menu);

  /**
   * Update menu.
   *
   * @param menu Menu to update
   */
  UUID update(Menu menu);

  /**
   * Delete menu.
   *
   * @param menu Menu to delete
   */
  void delete(Menu menu);

  /**
   * Find a menu using menuId.
   *
   * @param menuId id of menu
   * @param operationCode operation code
   * @param correlationId correlation id
   * @return menu if found else throw exception
   */
  Menu findMenuOrThrowException(UUID menuId, int operationCode, String correlationId);
}
