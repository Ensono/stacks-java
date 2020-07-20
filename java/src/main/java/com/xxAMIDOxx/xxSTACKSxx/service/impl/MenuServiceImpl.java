package com.xxAMIDOxx.xxSTACKSxx.service.impl;

import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.repository.MenuRepository;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.service.CosmosHelper.pageRequestWithSort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

// TODO need to capture the errors from the repository
@Service
public class MenuServiceImpl implements MenuService {

  private static final String NAME = "name";
  private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

  private final MenuRepository menuRepository;

  public MenuServiceImpl(MenuRepository menuRepository) {
    this.menuRepository = menuRepository;
  }

  public Optional<Menu> findById(UUID id) {
    return menuRepository.findById(id.toString());
  }

  public List<Menu> findAll(int pageNumber, int pageSize) {

    Page<Menu> page = menuRepository.findAll(
            pageRequestWithSort(Sort.Direction.ASC, NAME, 0, pageSize));

    int currentPage = 0;

    // This is specific and needed due to the way in which CosmosDB handles pagination
    // using a continuationToken and a limitation in the Swagger Specification.
    // See https://github.com/Azure/azure-sdk-for-java/issues/12726
    while (currentPage < pageNumber && page.hasNext()) {
      currentPage++;
      Pageable nextPageable = page.nextPageable();
      page = menuRepository.findAll(nextPageable);
    }

    return page.getContent();
  }

  @Override
  public List<Menu> findAllByRestaurantId(UUID restaurantId,
                                          Integer pageSize,
                                          Integer pageNumber) {

    return menuRepository.findAllByRestaurantId(
            restaurantId.toString(),
            pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
            .getContent();
  }

  @Override
  public List<Menu> findAllByNameContaining(String searchTerm,
                                            Integer pageSize,
                                            Integer pageNumber) {

    return menuRepository.findAllByNameContaining(
            searchTerm,
            pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
            .getContent();
  }

  @Override
  public List<Menu> findAllByRestaurantIdAndNameContaining(UUID restaurantId,
                                                           String searchTerm,
                                                           Integer pageSize,
                                                           Integer pageNumber) {

    return menuRepository.findAllByRestaurantIdAndNameContaining(
            restaurantId.toString(), searchTerm,
            pageRequestWithSort(Sort.Direction.ASC, NAME, pageNumber, pageSize))
            .getContent();
  }

  @Override
  public Menu saveMenu(Menu aMenu) {
    aMenu.setId(UUID.randomUUID().toString());
    Menu saved = menuRepository.save(aMenu);
    logger.debug("A new menu is created");
    return saved;
  }

  /**
   * Checks to see if a menu exists for the id provided
   * if exists then add the category to that menu.
   *
   * @param id       id of the menu category to be added to
   * @param category request Creating a Category and adding that to a Menu
   * @return category
   */
  @Override
  public Category saveCategory(UUID id, Category category) {
    Optional<Menu> optionalMenu = menuRepository.findById(id.toString());
    Menu menu = optionalMenu.get();

    if (isEmpty(menu.getId())) {
      return null;
    }

    category.setId(UUID.randomUUID().toString());
    menu.setCategories(List.of(category));
    menuRepository.save(menu);
    logger.debug("A new Category is added to the menu");
    return category;
  }

  @Override
  public String saveItem(Item item, String menuId, String categoryId) {
    Optional<Menu> optionalMenu = menuRepository.findById(menuId);
    Menu menu = optionalMenu.get();

    if (isEmpty(menu.getId()) || menu.getCategories() == null) {
      return null;
    }
    String itemId = null;

    for (Category t : menu.getCategories()) {
      if (t.getId().equals(categoryId)) {
        itemId = UUID.randomUUID().toString();
        item.setId(itemId);
        t.setItems(List.of(item));
        logger.debug("A new Item is added to the menu");
      }
    }
    menuRepository.save(menu);

    return itemId;
  }

}