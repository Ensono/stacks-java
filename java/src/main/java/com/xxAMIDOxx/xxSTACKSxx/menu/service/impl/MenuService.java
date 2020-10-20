package com.xxAMIDOxx.xxSTACKSxx.menu.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuUpdatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuNotFoundException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MenuService implements MenuQueryService {

  private static final String NAME = "name";
  private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

  private final MenuRepository menuRepository;
  private final ApplicationEventPublisher publisher;

  public MenuService(MenuRepository menuRepository, ApplicationEventPublisher publisher) {
    this.menuRepository = menuRepository;
    this.publisher = publisher;
  }

  public Optional<Menu> findById(UUID id) {
    return menuRepository.findById(id.toString());
  }

  public List<Menu> findAll(int pageNumber, int pageSize) {

    Page<Menu> page =
        menuRepository.findAll(PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)));

    // This is specific and needed due to the way in which CosmosDB handles pagination
    // using a continuationToken and a limitation in the Swagger Specification.
    // See https://github.com/Azure/azure-sdk-for-java/issues/12726
    int currentPage = 0;
    while (currentPage < pageNumber && page.hasNext()) {
      currentPage++;
      Pageable nextPageable = page.nextPageable();
      page = menuRepository.findAll(nextPageable);
    }
    return page.getContent();
  }

  @Override
  public List<Menu> findAllByRestaurantId(UUID restaurantId, Integer pageSize, Integer pageNumber) {

    return menuRepository
        .findAllByRestaurantId(
            restaurantId.toString(), PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public List<Menu> findAllByNameContaining(
      String searchTerm, Integer pageSize, Integer pageNumber) {

    return menuRepository
        .findAllByNameContaining(
            searchTerm, PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public List<Menu> findAllByRestaurantIdAndNameContaining(
      UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber) {

    return menuRepository
        .findAllByRestaurantIdAndNameContaining(
            restaurantId.toString(),
            searchTerm,
            PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public List<Menu> findAllByRestaurantIdAndName(
      UUID restaurantId, String searchTerm, Integer pageSize, Integer pageNumber) {

    return menuRepository
        .findAllByRestaurantIdAndName(
            restaurantId.toString(),
            searchTerm,
            PageRequest.of(0, pageSize, Sort.by(Sort.Direction.ASC, NAME)))
        .getContent();
  }

  @Override
  public UUID create(Menu menu) {
    menuRepository.save(menu);

    return UUID.fromString(menu.getId());
  }

  @Override
  public UUID update(Menu menu) {
    menuRepository.save(menu);

    return UUID.fromString(menu.getId());
  }

  @Override
  public void delete(Menu menu) {
    menuRepository.delete(menu);
  }

  /**
   * search for menu using id if not found throw exception.
   *
   * @param menuId id of menu
   * @param code operation code
   * @param correlationId correlation id
   * @return menu
   */
  public Menu findMenuOrThrowException(UUID menuId, int code, String correlationId) {
    return menuRepository
        .findById(menuId.toString())
        .orElseThrow(() -> new MenuNotFoundException(menuId.toString(), code, correlationId));
  }

  @Override
  public List<MenuEvent> menuCreatedEvents(int operationCode, String correlationId, UUID menuId) {
    return Collections.singletonList(new MenuCreatedEvent(operationCode, correlationId, menuId));
  }

  @Override
  public List<MenuEvent> menuUpdatedEvents(int operationCode, String correlationId, UUID menuId) {
    return Collections.singletonList(new MenuUpdatedEvent(operationCode, correlationId, menuId));
  }

  @Override
  public List<MenuEvent> menuDeletedEvents(int operationCode, String correlationId, UUID menuId) {
    return Collections.singletonList(new MenuDeletedEvent(operationCode, correlationId, menuId));
  }

  @Override
  public void publishEvents(List<MenuEvent> eventList) {
    eventList.forEach(publisher::publish);
  }
}
