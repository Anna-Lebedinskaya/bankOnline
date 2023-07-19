package com.bankproject.service;

import com.bankproject.dto.BankAccountDTO;
import com.bankproject.dto.TransactionDTO;
import com.bankproject.dto.TransferDTO;
import com.bankproject.mapper.GenericMapper;
import com.bankproject.model.BankAccount;
import com.bankproject.model.Transaction;
import com.bankproject.repository.BankAccountRepository;
import com.bankproject.repository.GenericRepository;
import com.bankproject.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionService
        extends GenericService<Transaction, TransactionDTO> {

    private final BankAccountService bankAccountService;

    public TransactionService(GenericRepository<Transaction> repository,
                              GenericMapper<Transaction, TransactionDTO> mapper,
                              BankAccountService bankAccountService) {
        super(repository, mapper);
        this.bankAccountService = bankAccountService;
    }

    @Override
    public TransactionDTO create(TransactionDTO newObject) {
        newObject.setCreatedWhen(LocalDateTime.now());
        newObject.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        newObject.setDate(LocalDate.now());

        BankAccountDTO bankAccountFrom = bankAccountService.getOne(newObject.getBankAccountFromId());
        Double newBalanceFrom = bankAccountFrom.getBalance() - newObject.getAmount();
        bankAccountFrom.setBalance(newBalanceFrom);
        bankAccountService.update(bankAccountFrom);

        BankAccountDTO bankAccountTo = bankAccountService.getOne(newObject.getBankAccountToId());
        Double newBalanceTo = bankAccountTo.getBalance() + newObject.getAmount();
        bankAccountTo.setBalance(newBalanceTo);
        bankAccountService.update(bankAccountTo);

        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    public Page<TransactionDTO> searchTransaction(TransferDTO transferDTO, PageRequest pageRequest) {
        Page<Transaction> transactionsPaginated = ((TransactionRepository) repository).searchTransactions(
                transferDTO.getAccountNumberFrom(),
                transferDTO.getAccountNumberTo(),
                transferDTO.getLastNameFrom(),
                transferDTO.getLastNameTo(),
                transferDTO.getTypeFrom(),
                transferDTO.getTypeTo(),
                transferDTO.getAmount(),
                transferDTO.getDate(),
                pageRequest);

        List<TransactionDTO> results = mapper.toDTOs(transactionsPaginated.getContent());
        return new PageImpl<>(results, pageRequest, transactionsPaginated.getTotalElements());
    }

    public Page<TransactionDTO> listUserTransactions(final Long id, final Pageable pageRequest) {
        Page<Transaction> objects = ((TransactionRepository) repository).searchTransactionsByUserId(id, pageRequest);
        List<TransactionDTO> results = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(results, pageRequest, objects.getTotalElements());
    }
}
