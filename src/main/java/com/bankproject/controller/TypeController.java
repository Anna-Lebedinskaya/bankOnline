package com.bankproject.controller;

import com.bankproject.dto.TypeDTO;
import com.bankproject.exception.MyDeleteException;
import com.bankproject.service.TypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/types")
public class TypeController extends MainController {

    private final TypeService typeService;

    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @GetMapping("/list")
    public String getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                         @RequestParam(value = "size", defaultValue = "5") int pageSize,
                         Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<TypeDTO> types = typeService.getAllTypes(pageRequest);
        model.addAttribute("types", types);
        return "types/viewAllTypes";
    }

    @GetMapping("/add")
    public String create(Model model) {
        model.addAttribute("typeForm", new TypeDTO());
        return "types/addType";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("typeForm") TypeDTO typeDTO,
                               BindingResult bindingResult) {
        if (typeService.getTypeByTitle(typeDTO.getTitle()) != null) {
            bindingResult.rejectValue("title", "error.title", "Такой продукт уже существует");
            return "add";
        }
        typeService.create(typeDTO);
        return "redirect:/types/list";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         Model model) {
        model.addAttribute("type", typeService.getOne(id));
        return "types/updateType";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("type") TypeDTO typeUpdatedDTO,
                         BindingResult bindingResult){
        TypeDTO typeDuplicated = typeService.getTypeByTitle(typeUpdatedDTO.getTitle());
        TypeDTO foundType = typeService.getOne(typeUpdatedDTO.getId());
        if (typeDuplicated != null && !Objects.equals(typeDuplicated.getTitle(), foundType.getTitle())) {
            bindingResult.rejectValue("title", "error.title", "Такой продукт уже существует");
            return "profile/updateProfile";
        }
        foundType.setTitle(typeUpdatedDTO.getTitle());
        foundType.setDescription(typeUpdatedDTO.getDescription());
        typeService.update(foundType);
        return "redirect:/types/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) throws MyDeleteException {
        typeService.deleteSoft(id);
        return "redirect:/types/list";
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id) {
        typeService.restore(id);
        return "redirect:/types/list";
    }

    @ExceptionHandler({MyDeleteException.class, AccessDeniedException.class, NotFoundException.class})
    public RedirectView handleError(HttpServletRequest request,
                                    Exception exception,
                                    RedirectAttributes redirectAttributes) {
        log.error("Запрос " + request.getRequestURL() + " вызвал ошибку: " + exception.getMessage());
        redirectAttributes.addFlashAttribute("exception", exception.getMessage());
        return new RedirectView("/types/list", true);
    }
}
