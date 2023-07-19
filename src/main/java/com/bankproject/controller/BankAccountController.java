package com.bankproject.controller;

import com.bankproject.dto.*;
import com.bankproject.service.BankAccountService;
import com.bankproject.service.TypeService;
import com.bankproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.bankproject.constants.UserRolesConstants.ADMIN;
import com.bankproject.exception.MyDeleteException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;

@Slf4j
@Controller
@RequestMapping("/bankAccounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final TypeService typeService;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService,
                                 TypeService typeService,
                                 UserService userService) {
        this.bankAccountService = bankAccountService;
        this.typeService = typeService;
        this.userService = userService;
    }

    @GetMapping
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         @ModelAttribute(name = "exception") final String exception,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "balance"));

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Page<BankAccountDTO> bankAccounts;

        if (ADMIN.equalsIgnoreCase(userName)) {
            bankAccounts = bankAccountService.listAll(pageRequest);
        }
        else {
            bankAccounts = bankAccountService.listAllNotDeleted(pageRequest);
        }

        model.addAttribute("exception", exception);
        model.addAttribute("bankAccounts", bankAccounts);
        return "bankAccounts/viewAllBankAccounts";
    }

    @PostMapping("/search")
    public String searchBankAccount(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                    @ModelAttribute("bankAccountSearchForm") BankAccountSearchDTO bankAccountSearchDTO,
                                    Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "balance"));
        model.addAttribute("bankAccounts", bankAccountService.searchBankAccount(bankAccountSearchDTO, pageRequest));
        return "bankAccounts/viewAllBankAccounts";
    }

    @GetMapping("/search/accountsByUser/{id}")
    public String searchUserAccounts(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "5") int pageSize,
                                     @PathVariable Long id,
                                     Model model) {
        BankAccountSearchDTO bankAccountSearchDTO = new BankAccountSearchDTO();
        bankAccountSearchDTO.setLastName(bankAccountService.getOne(id).getUserInfo().getLastName());
        return searchBankAccount(page, pageSize, bankAccountSearchDTO, model);
    }

    @GetMapping("/add")
    public String create(Model model) {
        List<TypeDTO> types = typeService.listAll();
        model.addAttribute("types", types);

        List<UserDTO> users = userService.listAll();
        model.addAttribute("users", users);

        return "bankAccounts/addBankAccount";
    }

    @PostMapping("/add")
    public String create(@RequestParam(name = "type") Long typeId,
                         @RequestParam(name = "user") Long userId) {
        BankAccountDTO bankAccountDTO = bankAccountService.create(typeId, userId);
        log.info("Created: {}", bankAccountDTO);
        return "redirect:/bankAccounts";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("bankAccount", bankAccountService.getOne(id));
        return "bankAccounts/updateBankAccount";
    }

    @PostMapping("/update")
    public String update(@RequestParam(name = "type") Long typeId,
                         @RequestParam(name = "user") Long userId,
                         @ModelAttribute("bankAccountForm") BankAccountDTO bankAccountDTO) {
        bankAccountDTO.setTypeInfo(typeService.getOne(typeId));
        bankAccountDTO.setUserInfo(userService.getOne(userId));
        bankAccountService.update(bankAccountDTO);
        log.info("Updated: {}", bankAccountDTO);
        return "redirect:/bankAccounts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        bankAccountService.deleteSoft(id);
        log.info("Soft Delete: {}", bankAccountService.getOne(id).toString());
        return "redirect:/bankAccounts";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        bankAccountService.restore(id);
        return "redirect:/bankAccounts";
    }

    @GetMapping("/close/{id}")
    public String close(@PathVariable Long id) throws MyDeleteException {
        bankAccountService.close(id);
        return "redirect:/bankAccounts";
    }

    @GetMapping("/user-accounts/{id}")
    public String userAccounts(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "size", defaultValue = "5") int pageSize,
                               @PathVariable Long id,
                               Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<BankAccountDTO> bankAccountDTOPage = bankAccountService.listUserBankAccounts(id, pageRequest);
        model.addAttribute("bankAccounts", bankAccountDTOPage);
        model.addAttribute("userId", id);
        return "userBankAccounts/viewAllUserAccounts";
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
