package com.xxAMIDOxx.xxSTACKSxx.service;

import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuService {

    /**
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<Menu> all(int pageNumber, int pageSize);

    /**
     * @param id
     * @return
     */
    Optional<Menu> findById(UUID id);

    /**
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


