package com.bankproject.mapper;

import com.bankproject.dto.TransactionDTO;
import com.bankproject.model.Transaction;
import com.bankproject.repository.BankAccountRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;

import java.util.List;

@Component
public class TransactionMapper extends GenericMapper<Transaction, TransactionDTO> {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public TransactionMapper(ModelMapper modelMapper,
                             BankAccountRepository bankAccountRepository,
                             BankAccountMapper bankAccountMapper) {
        super(Transaction.class, TransactionDTO.class, modelMapper);
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    protected void mapSpecificFields(TransactionDTO source, Transaction destination) {
        destination.setBankAccountFrom(bankAccountRepository.findById(source.getBankAccountFromId()).orElseThrow(() -> new NotFoundException("Счет не найден")));
        destination.setBankAccountTo(bankAccountRepository.findById(source.getBankAccountToId()).orElseThrow(() -> new NotFoundException("Счет не найден")));
    }

    @Override
    protected void mapSpecificFields(Transaction source, TransactionDTO destination) {
        destination.setBankAccountFromId(source.getBankAccountFrom().getId());
        destination.setBankAccountToId(source.getBankAccountTo().getId());
        destination.setBankAccountFromInfo(bankAccountMapper.toDTO(source.getBankAccountFrom()));
        destination.setBankAccountToInfo(bankAccountMapper.toDTO(source.getBankAccountTo()));
    }

    @Override
    @PostConstruct
    protected void setupMapper() {
        super.modelMapper.createTypeMap(Transaction.class, TransactionDTO.class)
                .addMappings(model -> {
                    model.skip(TransactionDTO::setBankAccountFromId);
                    model.skip(TransactionDTO::setBankAccountFromInfo);
                    model.skip(TransactionDTO::setBankAccountToId);
                    model.skip(TransactionDTO::setBankAccountToInfo);
                })
                .setPostConverter(toDTOConverter());

        super.modelMapper.createTypeMap(TransactionDTO.class, Transaction.class)
                .addMappings(model -> {
                    model.skip(Transaction::setBankAccountFrom);
                    model.skip(Transaction::setBankAccountTo);
                })
                .setPostConverter(toEntityConverter());
    }

    @Override
    protected List<Long> getIds(Transaction entity) {
        throw new UnsupportedOperationException("Метод недоступен");
    }
}