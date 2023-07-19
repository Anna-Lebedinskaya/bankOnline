package com.bankproject.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO extends GenericDTO{
    private String login;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String email;
    private String changePasswordToken;
    private RoleDTO role;
    private List<Long> bankAccountsIds;
}
