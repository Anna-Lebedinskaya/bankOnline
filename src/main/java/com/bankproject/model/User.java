package com.bankproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints =
                {@UniqueConstraint(name = "uniqueLogin", columnNames = "login")})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "users_seq", allocationSize = 1)
public class User extends GenericModel {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "change_password_token")
    private String changePasswordToken;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false,
            foreignKey = @ForeignKey(name = "FK_USERS_ROLES"))
    private Role role;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<BankAccount> bankAccounts;
}
