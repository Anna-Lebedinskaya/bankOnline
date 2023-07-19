package com.bankproject.controller;

import com.bankproject.constants.Errors;
import com.bankproject.dto.BankAccountDTO;
import com.bankproject.dto.TransactionDTO;
import com.bankproject.dto.TransferDTO;
import com.bankproject.exception.MyDeleteException;
import com.bankproject.service.BankAccountService;
import com.bankproject.service.TransactionService;
import com.bankproject.service.UserService;
import com.bankproject.service.userdetails.CustomUserDetails;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

import static com.bankproject.constants.UserRolesConstants.ADMIN;
import static com.bankproject.constants.UserRolesConstants.MANAGER;

@Slf4j
@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;

    public TransactionController(TransactionService transactionService,
                                 BankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "date", "amount"));
        Page<TransactionDTO> transactions = transactionService.listAll(pageRequest);
        model.addAttribute("exception", exception);
        model.addAttribute("transactions", transactions);
        return "transactions/viewAllTransactions";
    }

    @PostMapping("/search")
    public String searchTransactions(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                     @ModelAttribute("transactionSearchForm") TransferDTO transferDTO,
                                     Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "date", "amount"));
        model.addAttribute("transactions", transactionService.searchTransaction(transferDTO, pageRequest));
        return "transactions/viewAllTransactions";
    }

    @GetMapping("/user-transactions/{id}")
    public String userTransactions(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                   @PathVariable Integer id,
                                   Model model) throws AuthException {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!id.equals(customUserDetails.getUserId())) {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.Transaction.TRANSACTIONS_FORBIDDEN_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "date", "amount"));
        Page<TransactionDTO> transactions = transactionService.listUserTransactions(Long.valueOf(id), pageRequest);
        model.addAttribute("transactions", transactions);
        model.addAttribute("userId", id);
        return "userTransactions/viewAllTransactions";
    }

    @GetMapping("/{id}")
    public String create(@PathVariable Long id,
                         Model model) {
        model.addAttribute("bankAccountFrom", bankAccountService.getOne(id));
        model.addAttribute("transfer", new TransferDTO());
        return "transactions/transfer";
    }

    @PostMapping("/verify")
    public String create(@RequestParam(name = "id") Long bankAccountFromId,
                         @ModelAttribute("transfer") TransferDTO transfer,
                         BindingResult bindingResult,
                         Model model) {

        model.addAttribute("bankAccountFrom", bankAccountService.getOne(bankAccountFromId));

        List<BankAccountDTO> accountsTo = bankAccountService
                .searchBankAccountsToTransfer(transfer.getLastNameTo(),
                        transfer.getFirstNameTo(),
                        transfer.getMiddleNameTo(),
                        transfer.getAccountNumberTo());

        if (accountsTo.size() > 1) {
            bindingResult.rejectValue("lastNameTo", "error.lastNameTo", "У получателя несколько счетов. Укажите корректный номер счета получателя");
            return "transactions/transfer";
        }

        if (accountsTo.isEmpty() ||
            accountsTo.get(0).getAccountNumber().equals(bankAccountService.getOne(bankAccountFromId).getAccountNumber()) ||
            accountsTo.get(0).isClosed()) {
            bindingResult.rejectValue("accountNumberTo", "error.accountNumberTo", "Счет не найден! Укажите корректные данные получателя - ФИО или номер счета");
            return "transactions/transfer";
        }

        transfer.setAccountNumberTo(accountsTo.get(0).getAccountNumber());

        if (bankAccountService.getOne(bankAccountFromId).getBalance() < Double.parseDouble(transfer.getAmount())) {
            bindingResult.rejectValue("amount", "error.amount", "Недостаточно средств для перевода");
            return "transactions/transfer";
        }

        model.addAttribute("accountsTo", accountsTo);

        return "transactions/verify";
    }

    @PostMapping("/execute")
    public String create(@RequestParam(name = "bankAccountFromId") Long bankAccountFromId,
                         @RequestParam(name = "bankAccountToId") Long bankAccountToId,
                         @RequestParam(name = "amount") Double amount,
                         @RequestParam(name = "purpose") String purpose) throws AuthException {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBankAccountFromId(bankAccountFromId);
        transactionDTO.setBankAccountToId(bankAccountToId);
        transactionDTO.setAmount(amount);
        transactionDTO.setPurpose(purpose);

        transactionService.create(transactionDTO);

        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!Objects.isNull(customUserDetails.getUsername())) {
            if (ADMIN.equalsIgnoreCase(customUserDetails.getUsername()) || MANAGER.equalsIgnoreCase(customUserDetails.getUsername())) {
                return "redirect:/bankAccounts";
            } else {
                return "redirect:/bankAccounts/user-accounts/" + customUserDetails.getUserId();
            }
        } else {
            throw new AuthException(HttpStatus.FORBIDDEN + ": " + Errors.BankAccount.BANK_ACCOUNT_FORBIDDEN_ERROR);
        }
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        List<BankAccountDTO> bankAccounts = bankAccountService.listAll();
        model.addAttribute("bankAccounts", bankAccounts);

        model.addAttribute("transaction", transactionService.getOne(id));
        return "transactions/updateTransaction";
    }

    @PostMapping("/update")
    public String update(@RequestParam(name = "bankAccountFromId") Long bankAccountFromId,
                         @RequestParam(name = "bankAccountToId") Long bankAccountToId,
                         @RequestParam(name = "id") Long id) throws MyDeleteException {

        BankAccountDTO bankAccountFrom = bankAccountService.getOne(bankAccountFromId);
        BankAccountDTO bankAccountTo = bankAccountService.getOne(bankAccountToId);

        if(bankAccountFrom.getAccountNumber().equals(bankAccountTo.getAccountNumber())) {
            throw new MyDeleteException(Errors.Transaction.TRANSACTIONS_DELETE_ERROR);
        }

        TransactionDTO transaction = transactionService.getOne(id);
        transaction.setBankAccountFromInfo(bankAccountFrom);
        transaction.setBankAccountToInfo(bankAccountTo);

        transactionService.update(transaction);
        return "redirect:/transactions";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        transactionService.deleteSoft(id);
        return "redirect:/transactions";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        transactionService.restore(id);
        return "redirect:/transactions";
    }

    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class, NotFoundException.class})
    public RedirectView handleError(HttpServletRequest request,
                                    Exception exception,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос " + request.getRequestURL() + " вызвал ошибку: " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return new RedirectView("/bankAccounts", true);
    }
}
