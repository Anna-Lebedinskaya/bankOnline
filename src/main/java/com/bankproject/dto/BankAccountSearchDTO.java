package com.bankproject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BankAccountSearchDTO {
    private String typeTitle;
    private String accountNumber;
    private String lastName;
}