package com.bankproject.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BankAccountDTO extends GenericDTO{
    private UserDTO userInfo;
    private TypeDTO typeInfo;
    private String accountNumber;
    private Double balance;
    private boolean isClosed;
    private LocalDate openingDate;
    private LocalDate endingDate;
}
