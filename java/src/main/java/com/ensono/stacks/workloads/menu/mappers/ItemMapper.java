package com.ensono.stacks.workloads.menu.mappers;

import com.ensono.stacks.core.mapping.BaseMapper;
import com.ensono.stacks.workloads.menu.api.v1.dto.response.ItemDTO;
import com.ensono.stacks.workloads.menu.domain.Item;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ItemMapper extends BaseMapper<ItemDTO, Item> {}
