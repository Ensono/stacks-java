package com.ensono.stacks.workloads.menu.mappers;

import com.ensono.stacks.core.mapping.BaseMapper;

import com.ensono.stacks.core.mapping.MapperUtils;
import com.ensono.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import com.ensono.stacks.workloads.menu.domain.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {MapperUtils.class, CategoryMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MenuMapper extends BaseMapper<MenuDTO, Menu> {}
