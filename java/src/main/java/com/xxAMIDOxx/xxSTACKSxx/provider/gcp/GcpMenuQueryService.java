package com.xxAMIDOxx.xxSTACKSxx.provider.gcp;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.AzureMenu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "cloud.provider", havingValue = "gcp")
@Service("menuQueryService")
public class GcpMenuQueryService implements MenuQueryService {

  private static final String NAME = "name";

  private static Logger logger = LoggerFactory.getLogger(GcpMenuQueryService.class);

  private MenuRepositoryAdapter menuRepositoryAdapter;

  @Autowired
  public GcpMenuQueryService(MenuRepositoryAdapter menuRepositoryAdapter) {
    this.menuRepositoryAdapter = menuRepositoryAdapter;
  }

  public Optional<AzureMenu> findById(UUID id) {
    return menuRepositoryAdapter.findById(id.toString());
  }

  public List<AzureMenu> findAll(int pageNumber, int pageSize) {
    System.out.println("GOTCHA GcpMenuQueryService");
    Page<AzureMenu> page =
        menuRepositoryAdapter.findAll(PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)));

    return page.getContent();
  }

  @Override
  public List<AzureMenu> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber) {

    return menuRepositoryAdapter
        .findAllByRestaurantId(
            restaurantId.toString(), PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public List<AzureMenu> findAllByNameContaining(
      String searchTerm, Integer pageSize, Integer pageNumber) {

    return menuRepositoryAdapter
        .findAllByNameContaining(
            searchTerm, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public List<AzureMenu> findAllByRestaurantIdAndNameContaining(
      UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber) {

    return menuRepositoryAdapter
        .findAllByRestaurantIdAndNameContaining(
            restaurantId.toString(),
            searchTerm,
            PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }
}
