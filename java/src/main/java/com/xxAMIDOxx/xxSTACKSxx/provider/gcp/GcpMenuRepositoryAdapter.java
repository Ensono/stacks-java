package com.xxAMIDOxx.xxSTACKSxx.provider.gcp;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.GcpMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GcpMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected GcpMenuRepository gcpMenuRepository;

  public Page<AzureMenu> findAllByRestaurantId(String restaurantId, Pageable pageable) {
    List<GcpMenu> gcpMenus = gcpMenuRepository.findAllByRestaurantId(restaurantId, pageable).collectList().block();
    List<AzureMenu> menus = gcpMenus.stream().map(this::gcpMenuToMenu).collect(Collectors.toList());

    return new PageImpl<>(menus, pageable, menus.size());
  }

  @Override
  public AzureMenu save(AzureMenu menu) {
    GcpMenu gcpMenu = gcpMenuRepository.save(menuToGcpMenu(menu)).block();

    return gcpMenuToMenu(gcpMenu);
  }

  @Override
  public Page<AzureMenu> findAllByRestaurantIdAndName(String restaurantId, String name, Pageable pageable) {
    List<GcpMenu> gcpMenus = gcpMenuRepository.findAllByRestaurantIdAndName(restaurantId, name, pageable).collectList().block();
    List<AzureMenu> menus = gcpMenus.stream().map(this::gcpMenuToMenu).collect(Collectors.toList());

    return new PageImpl<>(menus, pageable, menus.size());
  }

  @Override
  public Page<AzureMenu> findAllByNameContaining(String searchTerm, Pageable pageable) {
    List<GcpMenu> gcpMenus = gcpMenuRepository.findAllByNameContaining(searchTerm, pageable).collectList().block();
    List<AzureMenu> menus = gcpMenus.stream().map(this::gcpMenuToMenu).collect(Collectors.toList());

    return new PageImpl<>(menus, pageable, menus.size());
  }

  @Override
  public Page<AzureMenu> findAllByRestaurantIdAndNameContaining(String restaurantId, String searchTerm, Pageable pageable) {
    List<GcpMenu> gcpMenus = gcpMenuRepository.findAllByRestaurantIdAndNameContaining(restaurantId, searchTerm, pageable).collectList().block();
    List<AzureMenu> menus = gcpMenus.stream().map(this::gcpMenuToMenu).collect(Collectors.toList());

    return new PageImpl<>(menus, pageable, menus.size());
  }

  @Override
  public Optional<AzureMenu> findById(String menuId) {
    GcpMenu gcpMenu = gcpMenuRepository.findById(menuId).block();
    if (gcpMenu != null) {
      return Optional.of(gcpMenuToMenu(gcpMenu));
    }
    return Optional.empty();
  }

  @Override
  public void delete(AzureMenu menu) {
    gcpMenuRepository.delete(menuToGcpMenu(menu)).block();
  }

  @Override
  public Page<AzureMenu> findAll(Pageable pageable) {
    List<GcpMenu> gcpMenus = gcpMenuRepository.findAll().collectList().block();
    List<AzureMenu> menus = gcpMenus.stream().map(this::gcpMenuToMenu).collect(Collectors.toList());

    return new PageImpl<>(menus, pageable, menus.size());
  }

  @Override
  public void deleteAll() {
    gcpMenuRepository.deleteAll().block();
  }

  private GcpMenu menuToGcpMenu(AzureMenu menu) {
    GcpMenu gcpMenu;
    gcpMenu = new GcpMenu(menu.getId(),
            menu.getRestaurantId(),
            menu.getName(),
            menu.getDescription(),
            menu.getCategories(),
            menu.getEnabled());

    return gcpMenu;
  }

  private AzureMenu gcpMenuToMenu(GcpMenu gcpMenu) {
    AzureMenu menu;
    menu = AzureMenu.builder()
            .id(gcpMenu.getId())
            .restaurantId(gcpMenu.getRestaurantId())
            .name(gcpMenu.getName())
            .description(gcpMenu.getDescription())
            .categories(gcpMenu.getCategories())
            .enabled(gcpMenu.getEnabled())
            .build();

    return menu;
  }
}
