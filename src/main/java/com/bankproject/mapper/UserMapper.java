package com.bankproject.mapper;

import com.bankproject.dto.UserDTO;
import com.bankproject.model.GenericModel;
import com.bankproject.model.User;
import com.bankproject.repository.BankAccountRepository;
import com.bankproject.repository.TypeRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {

    private final BankAccountRepository bankAccountRepository;
    private final TypeRepository typeRepository;

    public UserMapper(ModelMapper modelMapper,
                      BankAccountRepository bankAccountRepository,
                      TypeRepository typeRepository) {
        super(User.class, UserDTO.class, modelMapper);
        this.bankAccountRepository = bankAccountRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    protected void mapSpecificFields(UserDTO source, User destination) {
        if (!Objects.isNull(source.getBankAccountsIds())) {
            destination.setBankAccounts(bankAccountRepository.findAllById(source.getBankAccountsIds()));
        }
        else {
            destination.setBankAccounts(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(User source, UserDTO destination) {
        destination.setBankAccountsIds(getIds(source));
    }

    @Override
    @PostConstruct
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setBankAccountsIds))
                .setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setBankAccounts))
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected List<Long> getIds(User source) {
        return Objects.isNull(source) || Objects.isNull(source.getBankAccounts())
                ? null
                : source.getBankAccounts().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toList());
    }
}

