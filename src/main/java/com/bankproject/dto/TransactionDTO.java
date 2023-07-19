package com.bankproject.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransactionDTO extends GenericDTO {
    private Long bankAccountFromId;
    private Long bankAccountToId;
    private BankAccountDTO bankAccountFromInfo;
    private BankAccountDTO bankAccountToInfo;
    private Double amount;
    private String purpose;
    private LocalDate date;
}
