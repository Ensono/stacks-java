package com.xxAMIDOxx.xxSTACKSxx.menu.repository;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class GcpMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected AzureMenuRepository azureMenuRepository;

  public Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Menu save(Menu menu) {
    return azureMenuRepository.save(menu);
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndName(
      String restaurantId, String name, Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Optional<Menu> findById(String menuId) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public void delete(Menu menu) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Page<Menu> findAll(Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("GCP operation not supported");
  }
}
