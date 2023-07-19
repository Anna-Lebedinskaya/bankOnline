package com.bankproject.repository;

import com.bankproject.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository
        extends GenericRepository<BankAccount> {
    @Query(nativeQuery = true,
            value = """
                       SELECT DISTINCT ba.* FROM bank_accounts ba
                            JOIN types t ON ba.type_id = t.id
                            JOIN users u ON u.id = ba.user_id
                       WHERE t.title ilike '%' || coalesce(:typeTitle, '%')  || '%'
                            AND ba.account_number ilike '%' || coalesce(:accountNumber, '%')  || '%'
                            AND u.last_name ilike '%' || coalesce(:lastName, '%')  || '%'
                            AND ba.is_deleted = FALSE
                    """)
    Page<BankAccount> searchBankAccounts(@Param(value = "typeTitle") String typeTitle,
                                         @Param(value = "accountNumber") String accountNumber,
                                         @Param(value = "lastName") String lastName,
                                         Pageable pageRequest);

    Page<BankAccount> getBankAccountByUserId(Long id, Pageable pageRequest);

    @Query(nativeQuery = true,
            value = """
                       SELECT ba.* FROM bank_accounts ba
                           JOIN users u ON u.id = ba.user_id
                       WHERE u.last_name ilike '%' || coalesce(:lastNameTo, '%')  || '%'
                           AND u.first_name ilike '%' || coalesce(:firstNameTo, '%')  || '%'
                           AND u.middle_name ilike '%' || coalesce(:middleNameTo, '%')  || '%'
                           AND ba.account_number ilike '%' || coalesce(:accountNumberTo, '%')  || '%'
                           AND ba.is_deleted = FALSE
                    """)
    List<BankAccount> searchBankAccountsToTransfer(@Param(value = "lastNameTo") String lastNameTo,
                                                   @Param(value = "firstNameTo") String firstNameTo,
                                                   @Param(value = "middleNameTo") String middleNameTo,
                                                   @Param(value = "accountNumberTo") String accountNumberTo);

    @Query(nativeQuery = true,
            value = """
                     SELECT count(*)
                     FROM bank_accounts
                     WHERE created_when BETWEEN (now() - interval '7' day) AND now()
                   """)
    Integer report();
}
