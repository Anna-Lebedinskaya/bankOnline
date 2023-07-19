package com.bankproject.mapper;

import com.bankproject.dto.BankAccountDTO;
import com.bankproject.model.BankAccount;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankAccountMapper extends GenericMapper<BankAccount, BankAccountDTO> {

    private final TypeMapper typeMapper;
    private final UserMapper userMapper;

    public BankAccountMapper(ModelMapper modelMapper,
                             TypeMapper typeMapper,
                             UserMapper userMapper) {
        super(BankAccount.class, BankAccountDTO.class, modelMapper);
        this.typeMapper = typeMapper;
        this.userMapper = userMapper;
    }

    @Override
    protected void mapSpecificFields(BankAccountDTO source, BankAccount destination) {
        destination.setType(typeMapper.toEntity(source.getTypeInfo()));
        destination.setUser(userMapper.toEntity(source.getUserInfo()));
    }

    @Override
    protected void mapSpecificFields(BankAccount source, BankAccountDTO destination) {
        destination.setTypeInfo(typeMapper.toDTO(source.getType()));
        destination.setUserInfo(userMapper.toDTO(source.getUser()));
//        destination.setUserId(source.getUser().getId());
    }

    @Override
    @PostConstruct
    protected void setupMapper() {
        super.modelMapper.createTypeMap(BankAccount.class, BankAccountDTO.class)
                .addMappings(mapping -> {
                    mapping.skip(BankAccountDTO::setTypeInfo);
                    mapping.skip(BankAccountDTO::setUserInfo);
//                    mapping.skip(BankAccountDTO::setUserId);
                })
                .setPostConverter(toDTOConverter());

        super.modelMapper.createTypeMap(BankAccountDTO.class, BankAccount.class)
                .addMappings(m -> m.skip(BankAccount::setType))
                .addMappings(m -> m.skip(BankAccount::setUser))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected List<Long> getIds(BankAccount entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}
