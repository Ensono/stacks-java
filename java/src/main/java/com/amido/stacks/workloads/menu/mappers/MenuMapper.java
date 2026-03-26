package com.amido.stacks.workloads.menu.mappers;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.core.mapping.MapperUtils;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.amido.stacks.workloads.menu.domain.Menu;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {MapperUtils.class, CategoryMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuMapper extends BaseMapper<MenuDTO, Menu> {}
