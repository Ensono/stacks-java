package com.xxAMIDOxx.xxSTACKSxx.service;

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
     * @return
     */
    List<Menu> all(int pageNumber, int pageSize);

    /**
     * Retrieve Menu by Menu Id
     *
     * @param id
     * @return
     */
    Optional<Menu> findById(UUID id);

    /**
     * Retrieve Menu by Restaurant Id
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param pageable
     * @return
     */
    Page<Menu> findAllByRestaurantId(UUID restaurantId, Pageable pageable);


    /**
     * Retrieve Menu's by matching the name (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param searchTerm
     * @param pageable
     * @return
     */
    Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable);

    /**
     * Retrieve Menu's by matching the name and the restaurantId (Contains operation)
     * Pagination and sorting is done by spring data JPA.
     *
     * @param restaurantId
     * @param searchTerm
     * @param pageable
     * @return
     */
    Page<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId, String searchTerm, Pageable pageable);
}
