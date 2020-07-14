package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.model.Menu;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuService {

    /**
     * Retrieve list of all available Menus
     *
     * @param pageNumber
     * @param pageSize
     *
     * @return List of Menu
     */
    List<Menu> all(int pageNumber, int pageSize);

    /**
     * Retrieve Menu by Menu Id
     *
     * @param id
     *
     * @return Optional Menu
     */
    Optional<Menu> findById(UUID id);

    /**
     * Retrieve Menu by Restaurant Id
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param pageSize
     * @param pageNumber
     *
     * @return List of Menu
     */
    public List<Menu> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber);


    /**
     * Retrieve Menu's by matching the name (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     *
     * @return List of Menu
     */
    public List<Menu> findAllByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber);

    /**
     * Retrieve Menu's by matching the name and the restaurantId (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     * @return List of Menu
     */
    List<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber);
}
