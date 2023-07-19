package com.bankproject.mapper;

import com.bankproject.dto.GenericDTO;
import com.bankproject.model.GenericModel;

import java.util.List;

public interface Mapper<E extends GenericModel, D extends GenericDTO> {
    E toEntity(D dto);
    D toDTO(E entity);
    List<E> toEntities(List<D> dtos);
    List<D> toDTOs(List<E> entities);
}
