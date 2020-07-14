package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @return List of Menu's
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
     * @return List of SearchMenuResultItem
     */
    public List<SearchMenuResultItem> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber);


    /**
     * Retrieve Menu's by matching the name (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     *
     * @return List of SearchMenuResultItem
     */
    public List<SearchMenuResultItem> findAllByNameContaining(String searchTerm, Integer pageSize, Integer pageNumber);

    /**
     * Retrieve Menu's by matching the name and the restaurantId (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param searchTerm
     * @param pageSize
     * @param pageNumber
     *
     * @return List of SearchMenuResultItem
     */
    List<SearchMenuResultItem> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber);
}
