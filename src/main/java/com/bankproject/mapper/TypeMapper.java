package com.bankproject.mapper;

import com.bankproject.dto.TypeDTO;
import com.bankproject.model.Type;
import com.bankproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeMapper extends GenericMapper<Type, TypeDTO> {

//    private final UserRepository userRepository;

    public TypeMapper(ModelMapper modelMapper
//            , UserRepository userRepository
    ) {
        super(Type.class, TypeDTO.class, modelMapper);
//        this.userRepository = userRepository;
    }

    @Override
    protected void mapSpecificFields(Type source, TypeDTO destination) {
        throw new UnsupportedOperationException("Метод недоступен");
    }

    @Override
    protected void mapSpecificFields(TypeDTO source, Type destination) {
        throw new UnsupportedOperationException("Метод недоступен");
    }

    @Override
    @PostConstruct
    protected void setupMapper() { //TODO: переделать маппер
    }

    @Override
    protected List<Long> getIds(Type source) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}