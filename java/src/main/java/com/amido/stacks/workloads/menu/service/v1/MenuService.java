package com.amido.stacks.workloads.menu.service.v1;

import static java.util.Objects.nonNull;
import static java.util.UUID.fromString;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResult;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuAlreadyExistsException;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.MenuMapper;
import com.amido.stacks.workloads.menu.mappers.SearchMenuResultItemMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.CreateMenuMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.UpdateMenuMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.menu.service.data.MenuQueryService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  private final MenuQueryService menuQueryService;

  private final MenuMapper menuMapper;

  private final CreateMenuMapper createMenuMapper;

  private final UpdateMenuMapper updateMenuMapper;

  private final SearchMenuResultItemMapper searchMenuResultItemMapper;

  public MenuDTO create(@Valid CreateMenuRequest dto) {

    verifyMenuNotAlreadyExisting(dto.getTenantId(), dto.getName());

    MenuDTO menuDTO = createMenuMapper.fromDto(dto);
    menuDTO.setId(randomUUID());

    return (menuMapper.toDto(menuRepository.save(menuMapper.fromDto(menuDTO))));
  }

  public SearchMenuResult search(
      String searchTerm, UUID restaurantId, Integer pageSize, Integer pageNumber) {

    List<Menu> menuList;

    if (isNotEmpty(searchTerm) && nonNull(restaurantId)) {
      menuList =
          this.menuQueryService.findAllByRestaurantIdAndNameContaining(
              restaurantId, searchTerm, pageSize, pageNumber);
    } else if (isNotEmpty(searchTerm)) {
      menuList = this.menuQueryService.findAllByNameContaining(searchTerm, pageSize, pageNumber);
    } else if (nonNull(restaurantId)) {
      menuList = this.menuQueryService.findAllByRestaurantId(restaurantId, pageSize, pageNumber);
    } else {
      menuList = this.menuQueryService.findAll(pageNumber, pageSize);
    }

    return new SearchMenuResult(
        pageSize,
        pageNumber,
        menuList.stream().map(searchMenuResultItemMapper::toDto).collect(Collectors.toList()));
  }

  public MenuDTO get(UUID menuId) {

    Optional<Menu> optMenu = menuQueryService.findById(menuId);

    if (optMenu.isPresent()) {
      return menuMapper.toDto(optMenu.get());
    }

    throw new MenuNotFoundException(menuId);
  }

  public MenuDTO update(UUID menuId, @Valid UpdateMenuRequest dto) {

    Optional<Menu> optMenu = menuQueryService.findById(menuId);

    if (optMenu.isPresent()) {

      MenuDTO menuDTO = updateMenuMapper.fromDto(dto);
      menuDTO.setId(menuId);
      menuDTO.setRestaurantId(fromString(optMenu.get().getRestaurantId()));

      menuRepository.save(menuMapper.fromDto(menuDTO));

      return get(menuId);
    }

    throw new MenuNotFoundException(menuId);
  }

  public void verifyMenuNotAlreadyExisting(UUID restaurantId, String name) {
    Page<Menu> existing =
        menuRepository.findAllByRestaurantIdAndName(
            restaurantId.toString(), name, PageRequest.of(0, 1));

    if (!existing.getContent().isEmpty()
        && existing.get().anyMatch(m -> m.getName().equals(name))) {
      throw new MenuAlreadyExistsException(restaurantId, name);
    }
  }

  public void delete(UUID menuId) {

    Optional<Menu> optMenu = menuQueryService.findById(menuId);

    if (optMenu.isEmpty()) {
      throw new MenuNotFoundException(menuId);
    }

    menuRepository.delete(optMenu.get());
  }
}
