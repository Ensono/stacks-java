package com.amido.stacks.workloads.menu.mappers;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.core.mapping.MapperUtils;
import com.amido.stacks.workloads.menu.api.v1.dto.response.SearchMenuResultItem;
import com.amido.stacks.workloads.menu.domain.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {MapperUtils.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SearchMenuResultItemMapper extends BaseMapper<SearchMenuResultItem, Menu> {

  @Override
  @Mapping(target = "categories", ignore = true)
  Menu fromDto(SearchMenuResultItem arg0);

  @Override
  @Mapping(target = "categories", ignore = true)
  void updateFromDto(SearchMenuResultItem arg0, @MappingTarget Menu arg1);
}
