package com.bankproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "transactions_seq", allocationSize = 1)
public class Transaction extends GenericModel{
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id_from", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BANK_ACCOUNT_FROM"))
    private BankAccount bankAccountFrom;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id_to", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BANK_ACCOUNT_TO"))
    private BankAccount bankAccountTo;

    @Column(name = "amount", nullable = false)
    private  Double amount;

    @Column(name = "purpose", nullable = false)
    private  String purpose;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}
