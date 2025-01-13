package com.ensono.stacks.workloads.menu.mappers;

import com.ensono.stacks.core.mapping.BaseMapper;
import com.ensono.stacks.workloads.menu.api.v1.dto.response.CategoryDTO;
import com.ensono.stacks.workloads.menu.domain.Category;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {ItemMapper.class},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CategoryMapper extends BaseMapper<CategoryDTO, Category> {}
