package com.bankproject.constants;

public interface Errors {
    class User {
        public static final String USER_DELETE_ERROR = "Клиент не может быть удален, так как у него есть открытые счета";
    }

    class Transaction {
        public static final String TRANSACTIONS_DELETE_ERROR = "Счет не может быть закрыт/удален, так как его баланс не равен 0";

        public static final String TRANSACTIONS_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о движении средств";
    }

    class BankAccount{
        public static final String BANK_ACCOUNT_DELETE_ERROR = "Счет не может быть закрыт/удален, так как его баланас не равен 0";
        public static final String BANK_ACCOUNT_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о счетах";
    }

    class Users {
        public static final String USER_FORBIDDEN_ERROR = "У вас нет прав просматривать информацию о пользователе";
    }

    class Type {
        public static final String TYPE_DELETE_ERROR = "Банковский продукт не может быть удален, так как у него есть открытые счета";
    }
}
