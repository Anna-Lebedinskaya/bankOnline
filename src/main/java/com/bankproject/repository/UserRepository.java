package com.bankproject.repository;

import com.bankproject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
        extends GenericRepository<User> {

    User findUserByLogin(String login);

    User findUserByEmail(String email);

    User findUserByChangePasswordToken(String uuid);

    @Query(nativeQuery = true,
            value = """
                    select u.*
                    from users u
                    where u.first_name ilike '%' || coalesce(:firstName, '%') || '%'
                    and u.last_name ilike '%' || coalesce(:lastName, '%') || '%'
                    and u.login ilike '%' || coalesce(:login, '%') || '%'
                     """)
    Page<User> searchUsers(String firstName,
                           String lastName,
                           String login,
                           Pageable pageable);

    @Query(nativeQuery = true,
            value = """
                     SELECT CASE WHEN count(ba) > 0 then false else true end
                     FROM users
                     JOIN bank_accounts ba ON users.id = ba.user_id
                     WHERE users.id = :userId
                     AND ba.is_closed = false
                   """)
    boolean checkUserForDeletion(final Long userId);

    @Query(nativeQuery = true,
            value = """
                     SELECT count(*)
                     FROM users
                     WHERE created_when BETWEEN (now() - interval '7' day) AND now()
                   """)
    Integer report();
}
