package com.bankproject.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TransferDTO extends GenericDTO{
    private String lastNameFrom;
    private String firstNameFrom;
    private String middleNameFrom;
    private String accountNumberFrom;
    private String typeFrom;
    private String lastNameTo;
    private String firstNameTo;
    private String middleNameTo;
    private String accountNumberTo;
    private String typeTo;
    private String amount;
    private String purpose;
    private String date;
}
