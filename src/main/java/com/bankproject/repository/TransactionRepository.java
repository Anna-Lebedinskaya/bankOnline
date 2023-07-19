package com.bankproject.repository;

import com.bankproject.model.BankAccount;
import com.bankproject.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository
        extends GenericRepository<Transaction> {
    @Query(nativeQuery = true,
            value = """
                    SELECT tr.id AS tr_id,
                           tr.bank_account_id_from,
                           tr.bank_account_id_to,
                           tr.amount,
                           tr.date,
                           tr.created_by,
                           tr.created_when,
                           tr.deleted_by,
                           tr.deleted_when,
                           tr.is_deleted,
                           tr.purpose,
                                        
                           ba_from.id AS ba_from_id,
                           ba_from.user_id AS ba_from_user_id,
                           ba_from.type_id AS ba_from_type_id,
                           ba_from.account_number AS ba_from_account_number,
                                        
                           ba_to.id,
                           ba_to.user_id,
                           ba_to.type_id,
                           ba_to.account_number
                    FROM transactions tr
                             JOIN bank_accounts ba_from on ba_from.id = tr.bank_account_id_from
                             JOIN bank_accounts ba_to on ba_to.id = tr.bank_account_id_to
                             JOIN users u_from on u_from.id = ba_from.user_id
                             JOIN users u_to on u_to.id = ba_to.user_id
                             JOIN types t_from on t_from.id = ba_from.type_id
                             JOIN types t_to on t_to.id = ba_to.type_id
                    WHERE ba_from.account_number ilike '%' || coalesce(:accountNumberFrom, '%') || '%'
                        AND ba_to.account_number ilike '%' || coalesce(:accountNumberTo, '%') || '%'
                        AND u_from.last_name ilike '%' || coalesce(:lastNameFrom, '%') || '%'
                        AND u_to.last_name ilike '%' || coalesce(:lastNameTo, '%') || '%'
                        AND t_from.title ilike '%' || coalesce(:typeFrom, '%') || '%'
                        AND t_to.title ilike '%' || coalesce(:typeTo, '%') || '%'
                        AND cast(tr.amount as text) ilike '%' || coalesce(:amount, '%') || '%'
                        AND cast(tr.date as text) ilike '%' || coalesce(:date, '%') || '%'
                                         """)
    Page<Transaction> searchTransactions(String accountNumberFrom,
                                         String accountNumberTo,
                                         String lastNameFrom,
                                         String lastNameTo,
                                         String typeFrom,
                                         String typeTo,
                                         String amount,
                                         String date,
                                         Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                    SELECT tr.id AS tr_id,
                           tr.bank_account_id_from,
                           tr.bank_account_id_to,
                           tr.amount,
                           tr.date,
                           tr.created_by,
                           tr.created_when,
                           tr.deleted_by,
                           tr.deleted_when,
                           tr.is_deleted,
                           tr.purpose,
                                        
                           ba_from.id AS ba_from_id,
                           ba_from.user_id AS ba_from_user_id,
                           ba_from.type_id AS ba_from_type_id,
                           ba_from.account_number AS ba_from_account_number,
                                        
                           ba_to.id,
                           ba_to.user_id,
                           ba_to.type_id,
                           ba_to.account_number
                    FROM transactions tr
                             JOIN bank_accounts ba_from on ba_from.id = tr.bank_account_id_from
                             JOIN bank_accounts ba_to on ba_to.id = tr.bank_account_id_to
                    WHERE ba_from.account_number ilike '%' || coalesce(:accountNumber, '%') || '%'
                      AND ba_to.account_number ilike '%' || coalesce(:accountNumber, '%') || '%'
                    """)
    List<Transaction> searchTransactionsToMarkDeleted(final String accountNumber);

    @Query(nativeQuery = true,
            value = """
                    SELECT tr.id                  AS tr_id,
                           tr.bank_account_id_from,
                           tr.bank_account_id_to,
                           tr.amount,
                           tr.date,
                           tr.created_by,
                           tr.created_when,
                           tr.deleted_by,
                           tr.deleted_when,
                           tr.is_deleted,
                           tr.purpose,
                    
                           ba_from.id             AS ba_from_id,
                           ba_from.user_id        AS ba_from_user_id,
                           ba_from.type_id        AS ba_from_type_id,
                           ba_from.account_number AS ba_from_account_number,
                    
                           ba_to.id,
                           ba_to.user_id,
                           ba_to.type_id,
                           ba_to.account_number
                    FROM transactions tr
                             JOIN bank_accounts ba_from on ba_from.id = tr.bank_account_id_from
                             JOIN bank_accounts ba_to on ba_to.id = tr.bank_account_id_to
                             JOIN users u_from on u_from.id = ba_from.user_id
                    
                    WHERE u_from.id = :userId
                      AND tr.is_deleted = false
                                         """)
    Page<Transaction> searchTransactionsByUserId(final Long userId,
                                                 Pageable pageable);
}
