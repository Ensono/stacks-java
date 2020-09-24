package com.xxAMIDOxx.xxSTACKSxx.provider.gcp;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.GcpMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class GcpMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected GcpMenuRepository gcpMenuRepository;

  public Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable) {
    throw new UnsupportedOperationException("GCP operation not supported");
  }

  @Override
  public Menu save(Menu menu) {
    Mono test = gcpMenuRepository.save(menuToGcpMenu(menu));
    Object thing = test.block();
    return menu;
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndName(
      String restaurantId, String name, Pageable pageable) {
    // gcpMenuRepository.findAllByRestaurantIdAndName(restaurantId, name, pageable);

    return null;
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
    GcpMenu gcpMenu = gcpMenuRepository.findById(menuId).block();

    return Optional.of(gcpMenuToMenu(gcpMenu));
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

  private GcpMenu menuToGcpMenu(Menu menu) {
    GcpMenu gcpMenu = new GcpMenu(menu.getId(),
            menu.getRestaurantId(),
            menu.getName(),
            menu.getDescription(),
            menu.getCategories(),
            menu.getEnabled());

    return gcpMenu;
  }

  private Menu gcpMenuToMenu(GcpMenu gcpMenu) {
    Menu menu = Menu.builder()
            .id(gcpMenu.getId())
            .restaurantId(gcpMenu.getRestaurantId())
            .name(gcpMenu.getName())
            .description(gcpMenu.getDescription())
            .categories(gcpMenu.getCategories())
            .enabled(gcpMenu.getEnabled())
            .build();

//    Menu menu = new Menu(gcpMenu.getId(),
//            gcpMenu.getRestaurantId(),
//            gcpMenu.getName(),
//            gcpMenu.getDescription(),
//            gcpMenu.getCategories(),
//            gcpMenu.getEnabled());

    return menu;
  }
}
