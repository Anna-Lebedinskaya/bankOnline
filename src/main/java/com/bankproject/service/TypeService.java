package com.bankproject.service;

import com.bankproject.constants.Errors;
import com.bankproject.dto.TypeDTO;
import com.bankproject.exception.MyDeleteException;
import com.bankproject.mapper.TypeMapper;
import com.bankproject.model.Type;
import com.bankproject.repository.TypeRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TypeService
        extends GenericService<Type, TypeDTO> {
    public TypeService(TypeRepository repository, TypeMapper mapper) {
        super(repository, mapper);
    }

    public Page<TypeDTO> getAllTypes(Pageable pageable) {
        Page<Type> typesPaginated = repository.findAll(pageable);
        List<TypeDTO> result = mapper.toDTOs(typesPaginated.getContent());
        return new PageImpl<>(result, pageable, typesPaginated.getTotalElements());
    }

    public TypeDTO getTypeByTitle(final String title) {
        return mapper.toDTO(((TypeRepository) repository).findTypeByTitle(title));
    }

    @Override
    public TypeDTO create(TypeDTO newObject){
        newObject.setCreatedWhen(LocalDateTime.now());
        newObject.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    @Override
    public void deleteSoft(Long objectId) throws MyDeleteException {
        Type type = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Вида банковского продукта с заданным id=" + objectId + " не существует."));

        boolean typeCanBeDeleted = ((TypeRepository) repository).checkTypeForDeletion(objectId);

        if (typeCanBeDeleted) {
            markAsDeleted(type);
            repository.save(type);
        } else {
            throw new MyDeleteException(Errors.Type.TYPE_DELETE_ERROR);
        }
    }
}
