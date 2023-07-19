package com.bankproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/") //localhost:8080
    public String index() {
        return "index";
    }
}
