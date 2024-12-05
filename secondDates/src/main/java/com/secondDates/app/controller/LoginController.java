package com.secondDates.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logeatu")
public class LoginController {  

    @GetMapping("/erregistratu")
    public String registratu() {
        return "logeatu/register";  
    }
}
