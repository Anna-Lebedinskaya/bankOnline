package com.bankproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankProjectApplication implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Swagger path: http://localhost:8080/swagger-ui/index.html");
        System.out.println("Application path: http://localhost:8080");
    }

    public static void main(String[] args) {
        SpringApplication.run(BankProjectApplication.class, args);
    }

}
