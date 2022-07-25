package com.amido.stacks.workloads.menu.mappers.wrappers;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.request.CreateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CreateMenuMapper extends BaseMapper<CreateMenuRequest, MenuDTO> {

  @Override
  @Mapping(source = "restaurantId", target = "tenantId")
  CreateMenuRequest toDto(MenuDTO command);

  @Override
  @Mapping(source = "tenantId", target = "restaurantId")
  MenuDTO fromDto(CreateMenuRequest request);
}
