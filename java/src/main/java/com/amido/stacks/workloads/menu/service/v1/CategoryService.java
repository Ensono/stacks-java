package com.amido.stacks.workloads.menu.service.v1;

import static java.util.UUID.randomUUID;

import com.amido.stacks.core.api.dto.response.ResourceCreatedResponse;
import com.amido.stacks.core.api.dto.response.ResourceUpdatedResponse;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateCategoryRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.CategoryDTO;
import com.amido.stacks.workloads.menu.domain.Category;
import com.amido.stacks.workloads.menu.domain.Menu;
import com.amido.stacks.workloads.menu.exception.MenuNotFoundException;
import com.amido.stacks.workloads.menu.mappers.CategoryMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.CreateCategoryMapper;
import com.amido.stacks.workloads.menu.mappers.wrappers.UpdateCategoryMapper;
import com.amido.stacks.workloads.menu.repository.MenuRepository;
import com.amido.stacks.workloads.menu.service.data.MenuQueryService;
import com.amido.stacks.workloads.menu.service.v1.utility.MenuHelperService;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final MenuRepository menuRepository;

  private final MenuQueryService menuQueryService;

  private final MenuHelperService menuHelperService;

  private final CategoryMapper categoryMapper;

  private final CreateCategoryMapper createCategoryMapper;

  private final UpdateCategoryMapper updateCategoryMapper;

  public ResourceCreatedResponse create(
      UUID menuId, @Valid CreateCategoryRequest body, String correlationId) {

    Menu menu = getMenu(menuId);

    UUID categoryId = randomUUID();

    menuHelperService.verifyCategoryNameNotAlreadyExisting(menu, categoryId, body.getName());

    CategoryDTO categoryDTO = createCategoryMapper.fromDto(body);
    categoryDTO.setId(categoryId.toString());

    menuHelperService.addOrUpdateCategory(menu, categoryMapper.fromDto(categoryDTO));
    menuRepository.save(menu);

    return new ResourceCreatedResponse(categoryId);
  }

  public ResourceUpdatedResponse update(
      UUID menuId, UUID categoryId, @Valid UpdateCategoryRequest body, String correlationId) {

    Menu menu = getMenu(menuId);

    Category category = menuHelperService.checkCategoryExistsById(menu, categoryId);

    menuHelperService.verifyCategoryNameNotAlreadyExisting(menu, categoryId, body.getName());

    CategoryDTO updatedDTO = updateCategoryMapper.fromDto(body);
    updatedDTO.setId(categoryId.toString());

    Category updatedCategory = categoryMapper.fromDto(updatedDTO);
    updatedCategory.setItems(category.getItems());

    menuHelperService.addOrUpdateCategory(menu, updatedCategory);
    menuRepository.save(menu);

    return new ResourceUpdatedResponse(categoryId);
  }

  public void delete(UUID menuId, UUID categoryId, String correlationId) {

    Menu menu = getMenu(menuId);

    menuHelperService.checkCategoryExistsById(menu, categoryId);

    menuHelperService.removeCategory(menu, categoryId);
    menuRepository.save(menu);
  }

  private Menu getMenu(UUID menuId) {
    Optional<Menu> optMenu = menuQueryService.findById(menuId);

    if (optMenu.isEmpty()) {
      throw new MenuNotFoundException(menuId);
    }

    return optMenu.get();
  }
}
