package com.bankproject.controller;

import com.bankproject.dto.*;
import com.bankproject.service.BankAccountService;
import com.bankproject.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@Transactional
@Rollback(value = false)
public class UserControllerTest
        extends CommonTest {

    @Autowired
    private UserService userService;

    private final RoleDTO roleDTO = new RoleDTO(1L, "role_title", "role_description");

    private final UserDTO userDTO = new UserDTO("User_login",
            "User_password",
            "User_first_name",
            "User_middle_name",
            "User_last_name",
            LocalDate.now(),
            "User_phone",
            "User_address",
            "User_email@mail.ru",
            null,
            roleDTO,
            new ArrayList<>());

    private final UserDTO userDTOUpdated = new UserDTO("User_login",
            "User_password",
            "User_first_name",
            "User_middle_name",
            "User_last_name_updated",
            LocalDate.now(),
            "User_phone",
            "User_address",
            "User_email@mail.ru",
            null,
            roleDTO,
            new ArrayList<>());

    @Override
    @Test
    @DisplayName("Просмотр всех пользователей MVC контроллер")
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void getAll() throws Exception {
        log.info("Тест просмотра списка пользователей начат");
        mvc.perform(get("/users/list")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/viewAllUsers"))
                .andExpect(model().attributeExists("users"))
                .andReturn();
        log.info("Тест просмотра списка пользователей закончен");
    }

    @Override
    @Test
    @DisplayName("Создание нового пользователя через MVC контроллер")
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        log.info("Тест по созданию нового пользователя начат");
        mvc.perform(MockMvcRequestBuilders.post("/users/registration")
                        .flashAttr("userForm", userDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrlTemplate("/login"))
                .andExpect(redirectedUrl("/login"));
        log.info("Тест по созданию нового пользователя закончен!");
    }

    @Override
    @Order(2)
    @Test
    @DisplayName("Обновление профиля пользователя через MVC контроллер")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void updateObject() throws Exception {
//        log.info("Тест по обновлению профиля пользователя начат успешно");
////        userDTO.setLastName(userDTOUpdated.getLastName());
//        mvc.perform(post("/profile/update")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .flashAttr("userForm", userDTOUpdated)
//                        .accept(MediaType.APPLICATION_JSON_VALUE)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/users/profile/".concat(userDTO.getId().toString())))
//                .andExpect(redirectedUrl("/users/profile"));
//        log.info("Тест по обновлению профиля пользователя закончен успешно");
    }

    @Test
    @DisplayName("Восстановление удаленного банковского счета через MVC контроллер BankAccountController")
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void restore() throws Exception {
//        log.info("Тест по восстановлению удаленного банковского счета начат успешно");
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
//        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTO, pageRequest).getContent().get(0);
//        bankAccountService.deleteSoft(foundBankAccount.getId());
//        mvc.perform(get("/bankAccounts/restore/{id}", foundBankAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/bankAccounts"))
//                .andExpect(redirectedUrl("/bankAccounts"));
//
//        BankAccountDTO restoredBankAccount = bankAccountService.getOne(foundBankAccount.getId());
//        assertFalse(restoredBankAccount.isDeleted());
//        log.info("Тест по восстановлению удаленного банковского счета закончен успешно!");
    }

    @Test
    @DisplayName("Закрытие банковского счета через MVC контроллер BankAccountController")
    @Order(6)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void close() throws Exception {
//        log.info("Тест по закрытию удаленного банковского счета начат успешно");
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
//        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTO, pageRequest).getContent().get(0);
//        mvc.perform(get("/bankAccounts/close/{id}", foundBankAccount.getId())
//                        .param("page", "1")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/bankAccounts"))
//                .andExpect(redirectedUrl("/bankAccounts"));
//
//        BankAccountDTO closedBankAccount = bankAccountService.getOne(foundBankAccount.getId());
//        assertTrue(closedBankAccount.isClosed());
//        log.info("Тест по закрытию банковского счета закончен успешно!");
    }

    @Override
    @Test
    @DisplayName("Удаление банковского счета через MVC контроллер BankAccountController")
    @Order(7)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void deleteObject() throws Exception {
//        log.info("Тест по удалению банковского счета начат успешно");
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
//        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTOUpdated, pageRequest).getContent().get(0);
//        foundBankAccount.setDeleted(true);
//        mvc.perform(get("/bankAccounts/delete/{id}", foundBankAccount.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/bankAccounts"))
//                .andExpect(redirectedUrl("/bankAccounts"));
//
//        BankAccountDTO deletedBankAccount = bankAccountService.getOne(foundBankAccount.getId());
//        assertTrue(deletedBankAccount.isDeleted());
//        foundBankAccount.setDeleted(false);
//        log.info("Тест по soft удалению банковского счета закончен успешно!");
    }

    @Test
    @DisplayName("Просмотр банковских счетов конкретного пользователя через MVC контроллер BankAccountController")
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void userAccounts() throws Exception {
//        log.info("Тест просмотра банковских счетов конкретного пользователя начат");
//        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
//        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTOUpdated, pageRequest).getContent().get(0);
//        mvc.perform(get("/bankAccounts/user-accounts/{id}", foundBankAccount.getId())
//                        .param("page", "1")
//                        .param("size", "5")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("userBankAccounts/viewAllUserAccounts"))
//                .andExpect(model().attributeExists("bankAccounts"))
//                .andExpect(model().attributeExists("userId"))
//                .andReturn();
//        log.info("Тест просмотра банковских счетов конкретного пользователя закончен");
    }
}
