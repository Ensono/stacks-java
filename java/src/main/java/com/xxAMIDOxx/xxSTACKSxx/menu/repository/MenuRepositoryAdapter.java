package com.xxAMIDOxx.xxSTACKSxx.menu.repository;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Arathy Krishna
 * @author Sursh Krishnan
 * @author Steve Clewer
 */
public interface MenuRepositoryAdapter {

  Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable);

  Menu save(Menu menu);

  void delete(Menu menu);

  Optional<Menu> findById(String menuId);

  Page<Menu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable);

  Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable);

  Page<Menu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable);

  Page<Menu> findAll(Pageable pageable);

  void deleteAll();
}
