package com.bankproject.controller;

import com.bankproject.dto.*;
import com.bankproject.service.BankAccountService;
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
public class BankAccountControllerTest
        extends CommonTest {

    @Autowired
    private BankAccountService bankAccountService;

    private final RoleDTO roleDTO = new RoleDTO(1L, "role_title", "role_description");

    private final UserDTO userDTO = new UserDTO("User_login",
            "User_password",
            "User_first_name",
            "User_middle_name",
            "User_last_name",
            LocalDate.now(),
            "User_phone",
            "User_address",
            "User_email",
            null,
            roleDTO,
            new ArrayList<>());

    private final TypeDTO typeDTO = new TypeDTO("type_title", "type_description");

    private final BankAccountDTO bankAccountDTO = new BankAccountDTO(userDTO,
            typeDTO,
            "bankAccount_accountNumber",
            Double.parseDouble("0.0"),
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3));

    private final BankAccountDTO bankAccountDTOUpdated = new BankAccountDTO(userDTO,
            typeDTO,
            "bankAccount_accountNumber_updated",
            Double.parseDouble("0.0"),
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3));

    private final BankAccountSearchDTO bankAccountSearchDTO = new BankAccountSearchDTO(null,
            "bankAccount_accountNumber",
            null);

    private final BankAccountSearchDTO bankAccountSearchDTOUpdated = new BankAccountSearchDTO(null,
            "bankAccount_accountNumber_updated",
            null);

    @Override
    @Test
    @DisplayName("Просмотр всех банковских счетов через MVC контроллер BankAccountController")
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void getAll() throws Exception {
        log.info("Тест просмотра списка банковских счетов начат");
        mvc.perform(get("/bankAccounts")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bankAccounts/viewAllBankAccounts"))
                .andExpect(model().attributeExists("bankAccounts"))
                .andReturn();
        log.info("Тест просмотра списка банковских счетов закончен");
    }

    @Test
    @DisplayName("Поиск по критерием банковских счетов через MVC контроллер BankAccountController")
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void searchBankAccount() throws Exception {
        log.info("Тест просмотра списка банковских счетов по поиску по критериям начат");
        mvc.perform(post("/bankAccounts/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("bankAccountSearchForm", bankAccountSearchDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bankAccounts/viewAllBankAccounts"))
                .andExpect(model().attributeExists("bankAccounts"))
                .andReturn();
        log.info("Тест просмотра списка банковских счетов по поиску по критериям закончен");
    }

    @Override
    @Test
    @DisplayName("Создание банковского счета через MVC контроллер BankAccountController")
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        log.info("Тест по созданию банковского счета начат");
        mvc.perform(MockMvcRequestBuilders.post("/bankAccounts/add")
                        .param("type", "1")
                        .param("user", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bankAccounts"))
                .andExpect(redirectedUrlTemplate("/bankAccounts"))
                .andExpect(redirectedUrl("/bankAccounts"));
        log.info("Тест по созданию банковского счета закончен!");
    }

    @Override
    @Order(4)
    @Test
    @DisplayName("Обновление банковского счета через MVC контроллер BankAccountController")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению банковского счета начат успешно");
        bankAccountDTO.setAccountNumber(bankAccountDTOUpdated.getAccountNumber());
        mvc.perform(post("/bankAccounts/update")
                        .param("type", "1")
                        .param("user", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .flashAttr("bankAccountForm", bankAccountDTO)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bankAccounts"))
                .andExpect(redirectedUrl("/bankAccounts"));
        log.info("Тест по обновлению банковского счета закончен успешно");
    }

    @Test
    @DisplayName("Восстановление удаленного банковского счета через MVC контроллер BankAccountController")
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void restore() throws Exception {
        log.info("Тест по восстановлению удаленного банковского счета начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTO, pageRequest).getContent().get(0);
        bankAccountService.deleteSoft(foundBankAccount.getId());
        mvc.perform(get("/bankAccounts/restore/{id}", foundBankAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bankAccounts"))
                .andExpect(redirectedUrl("/bankAccounts"));

        BankAccountDTO restoredBankAccount = bankAccountService.getOne(foundBankAccount.getId());
        assertFalse(restoredBankAccount.isDeleted());
        log.info("Тест по восстановлению удаленного банковского счета закончен успешно!");
    }

    @Test
    @DisplayName("Закрытие банковского счета через MVC контроллер BankAccountController")
    @Order(6)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void close() throws Exception {
        log.info("Тест по закрытию удаленного банковского счета начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTO, pageRequest).getContent().get(0);
        mvc.perform(get("/bankAccounts/close/{id}", foundBankAccount.getId())
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bankAccounts"))
                .andExpect(redirectedUrl("/bankAccounts"));

        BankAccountDTO closedBankAccount = bankAccountService.getOne(foundBankAccount.getId());
        assertTrue(closedBankAccount.isClosed());
        log.info("Тест по закрытию банковского счета закончен успешно!");
    }

    @Override
    @Test
    @DisplayName("Удаление банковского счета через MVC контроллер BankAccountController")
    @Order(7)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void deleteObject() throws Exception {
        log.info("Тест по удалению банковского счета начат успешно");
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "balance"));
        BankAccountDTO foundBankAccount = bankAccountService.searchBankAccount(bankAccountSearchDTOUpdated, pageRequest).getContent().get(0);
        foundBankAccount.setDeleted(true);
        mvc.perform(get("/bankAccounts/delete/{id}", foundBankAccount.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bankAccounts"))
                .andExpect(redirectedUrl("/bankAccounts"));

        BankAccountDTO deletedBankAccount = bankAccountService.getOne(foundBankAccount.getId());
        assertTrue(deletedBankAccount.isDeleted());
        foundBankAccount.setDeleted(false);
        log.info("Тест по soft удалению банковского счета закончен успешно!");
    }
}
