package com.xxAMIDOxx.xxSTACKSxx.menu.repository;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AzureMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected AzureMenuRepository azureMenuRepository;

  public Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantId(restaurantId, pageable);
  }

  @Override
  public Menu save(Menu menu) {
    return azureMenuRepository.save(menu);
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndName(
      String restaurantId, String name, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantIdAndName(restaurantId, name, pageable);
  }

  @Override
  public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {
    return azureMenuRepository.findAllByNameContaining(searchTerm, pageable);
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantIdAndNameContaining(
        restaurantId, searchTerm, pageable);
  }

  @Override
  public Optional<Menu> findById(String menuId) {
    return azureMenuRepository.findById(menuId);
  }

  @Override
  public void delete(Menu menu) {
    azureMenuRepository.delete(menu);
  }

  @Override
  public Page<Menu> findAll(Pageable pageable) {
    return azureMenuRepository.findAll(pageable);
  }

  @Override
  public void deleteAll() {
    azureMenuRepository.deleteAll();
  }
}
