package com.bankproject.config;

import com.bankproject.repository.BankAccountRepository;
import com.bankproject.repository.UserRepository;
import com.bankproject.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.bankproject.constants.MailConstants.*;

@Component
@Slf4j
public class MailScheduler {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    private final JavaMailSender javaMailSender;

    public MailScheduler(UserRepository userRepository,
                         BankAccountRepository bankAccountRepository,
                         JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.javaMailSender = javaMailSender;
    }

    //https://crontab.cronhub.io/
    //https://crontab.guru/#15_14_1_*_*

    //Крон на каждую минуту: "0 0/1 * 1/1 * *"
    //"0 0 6 * * ?"
    @Scheduled(cron = "0 0 6 * * ?")
    public void sentMailsToReport() {
        log.info("Отправка отчета");
        SimpleMailMessage simpleMailMessage = MailUtils.createMailMessage(
                MAIL_ADDRESS_FOR_REPORT,
                MAIL_SUBJECT_FOR_REPORT,
                MAIL_MESSAGE_FOR_REPORT + "\n" +
                "Количество новых клиентов Банка:" + userRepository.report() + "\n" +
                "Количество открытых счетов:" + bankAccountRepository.report()
        );
        javaMailSender.send(simpleMailMessage);
    }
}
