package com.amido.stacks.core.mapping;

import java.util.List;
import org.mapstruct.MappingTarget;

public interface BaseMapper<D, E> {
    D toDto(E entity);

    E fromDto(D dto);

    void updateFromDto(D dto, @MappingTarget E entity);

    void updateFromEntity(E entity, @MappingTarget D dto);

    List<D> toDtoList(List<E> list);

    List<E> fromDtoList(List<D> list);
}