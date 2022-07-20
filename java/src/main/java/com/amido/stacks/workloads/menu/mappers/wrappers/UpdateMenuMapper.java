package com.amido.stacks.workloads.menu.mappers.wrappers;

import com.amido.stacks.core.mapping.BaseMapper;
import com.amido.stacks.workloads.menu.api.v1.dto.request.UpdateMenuRequest;
import com.amido.stacks.workloads.menu.api.v1.dto.response.MenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
    componentModel = "spring",
    uses = {},
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UpdateMenuMapper extends BaseMapper<UpdateMenuRequest, MenuDTO> {}
