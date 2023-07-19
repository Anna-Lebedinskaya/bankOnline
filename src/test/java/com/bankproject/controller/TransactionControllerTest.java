package com.bankproject.controller;

import com.bankproject.dto.*;
import com.bankproject.model.User;
import com.bankproject.service.TransactionService;
import com.bankproject.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
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
public class TransactionControllerTest
        extends CommonTest {

    @Autowired
    private TransactionService transactionService;

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
            "User_email",
            null,
            roleDTO,
            new ArrayList<>());

    private final TypeDTO typeDTO = new TypeDTO("type_title", "type_description");

    private final BankAccountDTO bankAccountDTO1 = new BankAccountDTO(userDTO,
            typeDTO,
            "accountNumber_1",
            Double.parseDouble("0.0"),
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3));

    private final BankAccountDTO bankAccountDTO2 = new BankAccountDTO(userDTO,
            typeDTO,
            "accountNumber_2",
            Double.parseDouble("0.0"),
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3));

    private final BankAccountDTO bankAccountDTOUpdated = new BankAccountDTO(userDTO,
            typeDTO,
            "accountNumber_3",
            Double.parseDouble("0.0"),
            false,
            LocalDate.now(),
            LocalDate.now().plusMonths(3));

    private final TransactionDTO transactionDTO = new TransactionDTO(bankAccountDTO1.getId(),
            bankAccountDTO2.getId(),
            bankAccountDTO1,
            bankAccountDTO2,
            Double.parseDouble("1000"),
            "bankAccount_purpose",
            LocalDate.now());

    private final TransactionDTO transactionDTOUpdated = new TransactionDTO(bankAccountDTO1.getId(),
            bankAccountDTO2.getId(),
            bankAccountDTO1,
            bankAccountDTOUpdated,
            Double.parseDouble("1000"),
            "bankAccount_purpose",
            LocalDate.now());

    private final TransferDTO transferDTO= new TransferDTO("User_last_name",
                                                        "User_first_name",
                                                        "User_middle_name",
                                                        "accountNumber_1",
                                                        "type_title",
                                                        "User_last_name",
                                                        "User_first_name",
                                                        "User_middle_name",
                                                        "accountNumber_2",
                                                        "type_title",
                                                        "500",
                                                        "purpose",
                                                        "2023-07-06");

    private final TransferDTO transferDTO2= new TransferDTO("User_last_name",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "123456",
            null,
            null);

    @Override
    @Test
    @DisplayName("Просмотр всех транзакций через MVC контроллер")
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void getAll() throws Exception {
        log.info("Тест просмотра списка транзакций счетов начат");
        mvc.perform(get("/transactions")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("transactions/viewAllTransactions"))
                .andExpect(model().attributeExists("transactions"))
                .andReturn();
        log.info("Тест просмотра списка транзакций счетов закончен");
    }

    @Test
    @DisplayName("Поиск по критерием транзакций через MVC контроллер")
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void searchTransactions() throws Exception {
        log.info("Тест просмотра списка транзакций по поиску по критериям начат");
        mvc.perform(post("/transactions/search")
                        .param("page", "1")
                        .param("size", "5")
                        .flashAttr("transactionSearchForm", transferDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("transactions/viewAllTransactions"))
                .andExpect(model().attributeExists("transactions"))
                .andReturn();
        log.info("Тест просмотра списка транзакций по поиску по критериям закончен");
    }

//    @Test
//    @DisplayName("Просмотр транзакций конкретного пользователя через MVC контроллер")
//    @Order(2)
//    @WithMockUser(username = "User_login", roles = "USER", password = "User_password")
//    protected void userTransactions() throws Exception {
//        log.info("Тест просмотра транзакций конкретного пользователя начат");
//        mvc.perform(get("/transactions/user-transactions/{id}", userService.getUserByEmail("User_email").getId())
//                        .param("page", "1")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("userTransactions/viewAllTransactions"))
//                .andExpect(model().attributeExists("transactions"))
//                .andExpect(model().attributeExists("userId"))
//                .andReturn();
//        log.info("Тест просмотра транзакций конкретного пользователя закончен");
//    }

    @Override
    @Test
    @DisplayName("Создание транзакции через MVC контроллер")
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void createObject() throws Exception {
        log.info("Тест по созданию транзакции начат");
        mvc.perform(MockMvcRequestBuilders.post("/transactions/verify")
                        .param("id", "1")
                        .flashAttr("transfer", transferDTO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("transactions/transfer"))
                .andReturn();
        log.info("Тест по созданию транзакции закончен!");
    }

    @Override
    @Order(3)
    @Test
    @DisplayName("Обновление транзакции через MVC контроллер BankAccountController")
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению транзакции начат успешно");
        transactionDTO.setBankAccountToInfo(transactionDTOUpdated.getBankAccountToInfo());
        mvc.perform(post("/transactions/update")
                        .param("bankAccountFromId", "1")
                        .param("bankAccountToId", "3")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/transactions"))
                .andExpect(redirectedUrl("/transactions"));
        log.info("Тест по обновлению транзакции закончен успешно");
    }

    @Test
    @DisplayName("Восстановление удаленной транзакции счета через MVC контроллер")
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void restore() throws Exception {
//        log.info("Тест по восстановлению удаленной транзакции начат успешно");
//        TransactionDTO foundTransaction = transactionService.create(new TransactionDTO(bankAccountDTO1.getId(),
//                bankAccountDTO2.getId(),
//                bankAccountDTO1,
//                bankAccountDTO2,
//                Double.parseDouble("1000"),
//                "purpose",
//                LocalDate.now()));
//        foundTransaction.setDeleted(true);
//        mvc.perform(get("/bankAccounts/restore/{id}", transactionService.findAllByPurpose("purpose").get(0).getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/transactions"))
//                .andExpect(redirectedUrl("/transactions"));

//        BankAccountDTO restoredBankAccount = bankAccountService.getOne(foundBankAccount.getId());
//        assertFalse(restoredBankAccount.isDeleted());
        log.info("Тест по восстановлению удаленной транзакции закончен успешно!");
    }

//    @Test
//    @DisplayName("Закрытие банковского счета через MVC контроллер BankAccountController")
//    @Order(6)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
//    protected void close() throws Exception {
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
//    }
//
//    @Override
//    @Test
//    @DisplayName("Удаление банковского счета через MVC контроллер BankAccountController")
//    @Order(7)
//    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
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

}
