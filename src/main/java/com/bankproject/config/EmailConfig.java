package com.bankproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String mailServerHost;
    @Value("${spring.mail.port}")
    private Integer mailServerPort;
    @Value("${spring.mail.username}")
    private String mailServerUserName;
    @Value("${spring.mail.password}")
    private String mailServerPassword;

    //    https://help.mail.ru/mail/mailer/popsmtp
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailServerUserName);
        mailSender.setPassword(mailServerPassword);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "false");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.mail.ru");

        return mailSender;
    }
}
