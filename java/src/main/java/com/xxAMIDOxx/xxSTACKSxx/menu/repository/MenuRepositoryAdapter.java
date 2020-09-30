package com.xxAMIDOxx.xxSTACKSxx.menu.repository;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 */
public interface MenuRepositoryAdapter {

  Page<AzureMenu> findAllByRestaurantId(String restaurantId, Pageable pageable);

  AzureMenu save(AzureMenu menu);

  void delete(AzureMenu menu);

  Optional<AzureMenu> findById(String menuId);

  Page<AzureMenu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable);

  Page<AzureMenu> findAllByNameContaining(String searchTerm, Pageable pageable);

  Page<AzureMenu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable);

  Page<AzureMenu> findAll(Pageable pageable);

  void deleteAll();
}
