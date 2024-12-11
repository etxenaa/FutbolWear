package com.secondDates.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/home")
public class LoginController {

    @Autowired
    private ErabiltzaileaRepository erabRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String login() {
        return "logeatu/logina";
    }

    @GetMapping("/futbolWear")
    public String index(Model model) {
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String rol = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority())
                .orElse(null);
        model.addAttribute("rola", rol);  // Pasar el rol al modelo
        return "home";
    }

    @GetMapping("/erregistratu")
    public String registratu(Model model) {
        model.addAttribute("erabiltzaileBerria", new Erabiltzailea());
        return "logeatu/register";
    }

    @PostMapping("/erregistratu")
    public String erregistratu(@ModelAttribute("erabiltzaileBerria") Erabiltzailea erab) {
        erab.setPasahitza(passwordEncoder.encode(erab.getPasahitza()));
        erab.setRola("User");
        erabRepo.save(erab);
        return "redirect:/home";
    }
    
    
}
