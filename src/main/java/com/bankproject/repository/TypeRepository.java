package com.bankproject.repository;

import com.bankproject.model.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository
        extends GenericRepository<Type> {

    Type findTypeByTitle(String title);

    @Query(nativeQuery = true,
            value = """
                     SELECT CASE WHEN count(ba) > 0 then false else true end
                     FROM types
                              JOIN bank_accounts ba on types.id = ba.type_id
                     WHERE types.id = :typeId
                       AND ba.is_closed = false
                   """)
    boolean checkTypeForDeletion(final Long typeId);

}
