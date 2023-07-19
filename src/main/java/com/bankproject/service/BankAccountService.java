package com.bankproject.service;

import com.bankproject.constants.Errors;
import com.bankproject.dto.BankAccountDTO;
import com.bankproject.dto.BankAccountSearchDTO;
import com.bankproject.exception.MyDeleteException;
import com.bankproject.mapper.BankAccountMapper;
import com.bankproject.model.BankAccount;
import com.bankproject.model.Transaction;
import com.bankproject.repository.BankAccountRepository;
import com.bankproject.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankAccountService extends GenericService<BankAccount, BankAccountDTO> {
    private final TypeService typeService;
    private final UserService userService;
    private final TransactionRepository transactionRepository;

    public BankAccountService(BankAccountRepository repository,
                              BankAccountMapper mapper,
                              TypeService typeService,
                              UserService userService,
                              TransactionRepository transactionRepository) {
        super(repository, mapper);
        this.typeService = typeService;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    public BankAccountDTO create(Long typeId, Long userId) {
        BankAccountDTO newBankAccount = new BankAccountDTO();
        newBankAccount.setCreatedWhen(LocalDateTime.now());
        newBankAccount.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        newBankAccount.setTypeInfo(typeService.getOne(typeId));
        newBankAccount.setUserInfo(userService.getOne(userId));
        newBankAccount.setAccountNumber("4081781009991".
                concat(String.valueOf((int) (Math.random() * 10000000))));
        newBankAccount.setBalance(0.0);
        newBankAccount.setOpeningDate(LocalDate.now());
        return mapper.toDTO(repository.save(mapper.toEntity(newBankAccount)));
    }

    public Page<BankAccountDTO> searchBankAccount(BankAccountSearchDTO bankAccountSearchDTO, Pageable pageRequest) {
        Page<BankAccount> bankAccountsPaginated = ((BankAccountRepository) repository).searchBankAccounts(
                bankAccountSearchDTO.getTypeTitle(),
                bankAccountSearchDTO.getAccountNumber(),
                bankAccountSearchDTO.getLastName(),
                pageRequest
        );

        List<BankAccountDTO> result = mapper.toDTOs(bankAccountsPaginated.getContent());
        return new PageImpl<>(result, pageRequest, bankAccountsPaginated.getTotalElements());
    }

    public Page<BankAccountDTO> listUserBankAccounts(final Long id, final Pageable pageRequest) {
        Page<BankAccount> objects = ((BankAccountRepository) repository).getBankAccountByUserId(id, pageRequest);
        List<BankAccountDTO> results = mapper.toDTOs(objects.getContent());
        return new PageImpl<>(results, pageRequest, objects.getTotalElements());
    }

    public List<BankAccountDTO> searchBankAccountsToTransfer(String lastNameTo,
                                                             String firstNameTo,
                                                             String middleNameTo,
                                                             String accountNumberTo) {
        List<BankAccount> accounts =
                ((BankAccountRepository) repository)
                        .searchBankAccountsToTransfer(lastNameTo, firstNameTo, middleNameTo, accountNumberTo);

        return mapper.toDTOs(accounts);
    }

    @Override
    public void deleteSoft(Long objectId) throws MyDeleteException {
        BankAccount bankAccount = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Банковского счета с заданным id=" + objectId + " не существует."));

        if (bankAccount.getBalance() < 0.0000001) {
            markAsDeleted(bankAccount);

            List<Transaction> transactions = transactionRepository.searchTransactionsToMarkDeleted(bankAccount.getAccountNumber());

            if (!transactions.isEmpty()) {
                transactions.forEach(this::markAsDeleted);
                transactionRepository.saveAll(transactions);
            }

            repository.save(bankAccount);

        } else {
            throw new MyDeleteException(Errors.BankAccount.BANK_ACCOUNT_DELETE_ERROR);
        }
    }

    public void restore(Long objectId) {
        BankAccount bankAccount = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Банковского счета с заданным id=" + objectId + " не существует."));
        unMarkAsDeleted(bankAccount);
        repository.save(bankAccount);
    }

    public void close(Long objectId) throws MyDeleteException {
        BankAccount bankAccount = repository.findById(objectId).orElseThrow(
                () -> new NotFoundException("Банковского счета с заданным id=" + objectId + " не существует."));

        if (bankAccount.getBalance() < 0.0000001) {
            bankAccount.setClosed(true);
            bankAccount.setEndingDate(LocalDate.now());
            repository.save(bankAccount);
        } else {
            throw new MyDeleteException(Errors.BankAccount.BANK_ACCOUNT_DELETE_ERROR);
        }
    }
}
