package com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.MenuController;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResult;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.SearchMenuResultItem;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.CreateCategoryRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.CreateItemRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.requestDto.MenuCreateRequestDto;
import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responseDto.ResourceCreatedResponseDto;
import com.xxAMIDOxx.xxSTACKSxx.model.Category;
import com.xxAMIDOxx.xxSTACKSxx.model.Item;
import com.xxAMIDOxx.xxSTACKSxx.model.Menu;
import com.xxAMIDOxx.xxSTACKSxx.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

/**
 * MenuControllerImpl - Menu Controller used to interact and manage menus API.
 *
 * @author James Peet
 * @author James Peet
 * @author Suresh Krishnan
 * @author ArathyKrishna
 */
@RestController
public class MenuControllerImpl implements MenuController {

  private final MenuService menuService;
  private final ObjectMapper mapper;

  public MenuControllerImpl(MenuService menuService, ObjectMapper mapper) {
    this.menuService = menuService;
    this.mapper = mapper;
  }

  @Override
  public ResponseEntity<SearchMenuResult> searchMenu(final String searchTerm,
                                                     final UUID restaurantId,
                                                     final Integer pageSize,
                                                     final Integer pageNumber) {
    List<Menu> menuList;

    if (isNotBlank(searchTerm) && nonNull(restaurantId)) {
      menuList = this.menuService.findAllByRestaurantIdAndNameContaining(
              restaurantId, searchTerm, pageSize, pageNumber);
    } else if (isNotEmpty(searchTerm)) {
      menuList =
              this.menuService.findAllByNameContaining(searchTerm, pageSize, pageNumber);
    } else if (nonNull(restaurantId)) {
      menuList =
              this.menuService.findAllByRestaurantId(restaurantId, pageSize, pageNumber);
    } else {
      menuList = this.menuService.findAll(pageNumber, pageSize);
    }

    return ResponseEntity.ok(new SearchMenuResult(pageSize, pageNumber,
            menuList.stream().map(SearchMenuResultItem::new).collect(Collectors.toList())));
  }

  @Override
  public ResponseEntity<Menu> getMenu(UUID id) {
    return ResponseEntity.of(this.menuService.findById(id));
  }

  /**
   * @param requestDto menu to be created
   * @return ResourceCreatedResponseDto
   */
  @Override
  public ResponseEntity<ResourceCreatedResponseDto> createMenu(
          MenuCreateRequestDto requestDto) {
    Menu menu = this.menuService.saveMenu(convertMenuDtoToMenu(requestDto));
    return createdResponse(menu.getId(), CREATED);
  }

  /**
   * Mapping MenuRequestDto to Menu object
   *
   * @param requestDto Menu request dto
   * @return mapped menu
   */
  private Menu convertMenuDtoToMenu(MenuCreateRequestDto requestDto) {
    Menu newMenu = this.mapper.convertValue(requestDto, Menu.class);
    if (isNotBlank(requestDto.getTenantId())) {
      newMenu.setRestaurantId(UUID.fromString(requestDto.getTenantId()));
    }
    return newMenu;
  }

  /**
   * @param requestDto dto mapping Create category request
   * @return createdResponse
   */
  @Override
  public ResponseEntity<ResourceCreatedResponseDto>
  createCategory(UUID id, CreateCategoryRequestDto requestDto) {
    Category category = convertCategoryDtoToCategory(requestDto);

    if (isNull(category)) {
      return new ResponseEntity<>(BAD_REQUEST);
    }
    category = this.menuService.saveCategory(id, category);

    if (isNull(category)) {
      return new ResponseEntity<>(NOT_FOUND);
    }

    return createdResponse(category.getId(), OK);
  }

  /**
   * Mapping MenuRequestDto to Menu object
   *
   * @param requestDto Menu request dto
   * @return mapped menu
   */
  private Category convertCategoryDtoToCategory(
          CreateCategoryRequestDto requestDto) {
    return this.mapper.convertValue(requestDto, Category.class);
  }

  /**
   * @param id         Menu id
   * @param categoryId Id for the category item to be added
   * @param requestDto Create item request dto
   * @return createdResponse
   */
  @Override
  public ResponseEntity<ResourceCreatedResponseDto> createItems(String id,
                                                                String categoryId,
                                                                CreateItemRequestDto requestDto) {
    Item item = convertItemDtoToItem(requestDto);

    if (isBlank(id) || isBlank(categoryId) || isNull(item)) {
      return new ResponseEntity<>(BAD_REQUEST);
    }

    String itemId = this.menuService.saveItem(item, id, categoryId);
    if (isBlank(itemId)) {
      return new ResponseEntity<>(NOT_FOUND);
    }
    return createdResponse(itemId, CREATED);
  }

  private Item convertItemDtoToItem(CreateItemRequestDto requestDto) {
    return this.mapper.convertValue(requestDto, Item.class);
  }

  private ResponseEntity<ResourceCreatedResponseDto> createdResponse(String id,
                                                                     HttpStatus status) {
    ResourceCreatedResponseDto createdResponse =
            new ResourceCreatedResponseDto();
    createdResponse.setId(id);

    return new ResponseEntity<>(createdResponse, status);
  }

}
