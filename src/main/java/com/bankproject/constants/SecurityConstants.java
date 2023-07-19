package com.bankproject.constants;

import java.util.List;

public interface SecurityConstants {
    List<String> RESOURCES_WHITE_LIST = List.of("/resources/**",
            "/static/**",
            "/js/**",
            "/css/**",
            "/img/**",
            "/carousel/**",
            "/error",
            "/",
            "/swagger-ui/**",
            "/webjars/bootstrap/5.3.0/**",
            "/webjars/bootstrap/5.3.0/css/**",
            "/webjars/bootstrap/5.3.0/js/**",
            "/v3/api-docs/**");

    List<String> TRANSACTIONS_PERMISSION_LIST = List.of("/transactions",
            "/transactions/search");

//    List<String> AUTHORS_WHITE_LIST = List.of("/authors",
//            "/authors/search",
//            "/books/search/booksByAuthor",
//            "/authors/{id}");

    List<String> BANK_ACCOUNTS_PERMISSION_LIST = List.of("/bankAccounts",
            "/bankAccounts/add",
            "/bankAccounts/search",
            "/bankAccounts/update",
            "/bankAccounts/delete",
            "/bankAccounts/restore",
            "/bankAccounts/close",
            "/bankAccounts//user-accounts/{id}");

    List<String> TYPES_PERMISSION_LIST = List.of("/types",
            "/types/add",
            "/types/list",
            "/types/update",
            "/types/delete");

    List<String> USERS_WHITE_LIST = List.of(
            "/login",
            "/users/registration",
            "/users/remember-password",
            "/users/change-password");

    List<String> USERS_PERMISSION_LIST = List.of("/users/profile/update*",
            "/bankAccounts/user-accounts/transactions*," +
//            "/transactions",
            "/transactions/verify",
            "/transactions/execute");



//    List<String> USERS_REST_WHITE_LIST = List.of("/users/auth");
}
