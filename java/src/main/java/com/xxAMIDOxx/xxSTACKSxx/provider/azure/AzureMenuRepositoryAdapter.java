package com.xxAMIDOxx.xxSTACKSxx.provider.azure;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;

import java.util.Optional;

import com.xxAMIDOxx.xxSTACKSxx.menu.mappers.DomainToDtoMapper;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AzureMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected AzureMenuRepository azureMenuRepository;

  @Autowired protected DomainToDtoMapper mapper;

  public Page<AzureMenu> findAllByRestaurantId(String restaurantId, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantId(restaurantId, pageable);

//    Page<Menu> menus = azureMenuRepository.findAllByRestaurantId(restaurantId, pageable)
//                    .map(m -> mapper.toMenuDto(m));
//    return menus;
  }

  @Override
  public AzureMenu save(AzureMenu menu) {
    return azureMenuRepository.save(menu);
//    Menu result = azureMenuRepository.save(mapper.toAzureMenu(menu));
//    return mapper.toMenuDto(result);
  }

  @Override
  public Page<AzureMenu> findAllByRestaurantIdAndName(
      String restaurantId, String name, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantIdAndName(restaurantId, name, pageable);
//    Page<MenuDTO> menus = azureMenuRepository.findAllByRestaurantIdAndName(restaurantId, name, pageable)
//            .map(m -> mapper.toMenuDto(m));
//    return menus;
  }

  @Override
  public Page<AzureMenu> findAllByNameContaining(String searchTerm, Pageable pageable) {
    return azureMenuRepository.findAllByNameContaining(searchTerm, pageable);
//    Page<MenuDTO> menus = azureMenuRepository.findAllByNameContaining(searchTerm, pageable)
//            .map(m -> mapper.toMenuDto(m));
//    return menus;
  }

  @Override
  public Page<AzureMenu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable) {
    return azureMenuRepository.findAllByRestaurantIdAndNameContaining(
        restaurantId, searchTerm, pageable);
//    Page<Menu> menus = azureMenuRepository.findAllByRestaurantIdAndNameContaining(restaurantId, searchTerm, pageable)
//            .map(m -> mapper.toMenuDto(m));
//    return menus;
  }

  @Override
  public Optional<AzureMenu> findById(String menuId) {
    return azureMenuRepository.findById(menuId);
//    Optional<Menu> result = azureMenuRepository.findById(menuId);
//    if (result.isPresent()) {
//      return Optional.of(mapper.toMenuDto(result.get()));
//    }
//    return Optional.empty();
  }

  @Override
  public void delete(AzureMenu menu) {
    azureMenuRepository.delete(menu);
//    azureMenuRepository.delete(mapper.toAzureMenu(menu));
  }

  @Override
  public Page<AzureMenu> findAll(Pageable pageable) {
    return azureMenuRepository.findAll(pageable);
//    Page<Menu> menus = azureMenuRepository.findAll(pageable)
//            .map(m -> mapper.toMenuDto(m));
//    return menus;
  }

  @Override
  public void deleteAll() {
    azureMenuRepository.deleteAll();
  }
}
