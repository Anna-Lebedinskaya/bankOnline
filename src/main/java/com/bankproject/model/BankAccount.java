package com.bankproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bank_accounts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "bank_accounts_seq", allocationSize = 1)
public class BankAccount extends GenericModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BANK_ACCOUNTS_USERS"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_BANK_ACCOUNTS_TYPES"))
    private Type type;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @Column(name = "is_closed", nullable = false)
    private boolean isClosed;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "ending_date")
    private LocalDate endingDate;
}
