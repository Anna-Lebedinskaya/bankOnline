package com.bankproject.constants;

public interface MailConstants {
    String MAIL_MESSAGE_FOR_REMEMBER_PASSWORD = """
          Добрый день. Вы получили это письмо, так как с вашего аккаунта была отправлена заявка на восстановление пароля.
          Для восстановления пароля перейдите по ссылке: http://localhost:8080/users/change-password?uuid=""";

    String MAIL_SUBJECT_FOR_REMEMBER_PASSWORD = "Восстановление пароля на сайте Онлайн Банк";

    String MAIL_MESSAGE_FOR_REPORT = """
          Добрый день.
          Сформирован отчет по открытым счетам и транзакциям за прошедшую неделю""";

    String MAIL_SUBJECT_FOR_REPORT = "Отчет по открытым счетам и транзакциям за прошедшую неделю";

    String MAIL_ADDRESS_FOR_REPORT = "annavolchkova@mail.ru";
}
