package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuService {

  /**
   * Retrieve list of all available Menus
   *
   * @param pageNumber pageNo
   * @param pageSize   pageSize
   * @return List of Menu
   */
  List<Menu> findAll(int pageNumber, int pageSize);

  /**
   * Retrieve Menu by Menu Id
   *
   * @param id menuId
   * @return Optional Menu
   */
  Optional<Menu> findById(UUID id);

  /**
   * Retrieve Menu by Restaurant Id
   * Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @param pageSize     pageSize
   * @param pageNumber   pageNo
   * @return List of Menu
   */
  List<Menu> findAllByRestaurantId(UUID restaurantId, Integer pageSize,
                                   Integer pageNumber);


  /**
   * Retrieve Menu's by matching the name (Contains operation)
   * Pagination and sorting is done by spring data JPA.
   *
   * @param searchTerm menu Name
   * @param pageSize   pageSize
   * @param pageNumber pageNo
   * @return List of Menu
   */
  List<Menu> findAllByNameContaining(String searchTerm, Integer pageSize,
                                     Integer pageNumber);

  /**
   * Retrieve Menu's by matching the name and the restaurantId (Contains operation)
   * Pagination and sorting is done by spring data JPA.
   *
   * @param restaurantId tenantID/RestaurantId
   * @param searchTerm   Menu Name
   * @param pageSize     pageSize
   * @param pageNumber   pageNo
   * @return List of Menu
   */
  List<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId,
                                                    String searchTerm,
                                                    Integer pageSize,
                                                    Integer pageNumber);

  /**
   * Creates a Menu
   *
   * @param aMenu for creating a menu object
   * @return created Menu
   */
  Menu saveMenu(Menu aMenu);

  /**
   * creates a Category
   *
   * @param category for adding a category to a Menu
   * @return created Category
   */
  Category saveCategory(UUID id, Category category);

  /**
   * Create
   * @param item item request to save
   * @param menuId Menu id to add the item to
   * @param categoryId Id for the category item to be added
   * @return String item ID
   */
  String saveItem(Item item, String menuId, String categoryId);
}
