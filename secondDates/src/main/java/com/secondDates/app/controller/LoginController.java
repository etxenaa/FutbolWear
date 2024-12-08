package com.secondDates.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.modelo.Produktua;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private ErabiltzaileaRepository erabRepo;
	
	@GetMapping("/erregistratu")
	public String registratu(Model model) {
		model.addAttribute("erabiltzaileBerria", new Erabiltzailea());
		return "logeatu/register";
	}
	
	@PostMapping("/erregistratu")
	public String erregistratu(@ModelAttribute("erabiltzaileBerria") Erabiltzailea erab) {
		erab.setRola("User");
		erabRepo.save(erab);
		return "redirect:/erabiltzaileak/ikusi";
	}
	

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("erabiltzaileLogin", new Erabiltzailea());
		return "logeatu/logina";
	}
	
	@PostMapping("/login")
	public String login2(@ModelAttribute("erabiltzaileLogin") Erabiltzailea erab, Model model) {
		String email = erab.getEmail();
	    String pasahitza = erab.getPasahitza();
	    boolean emailAurkituta = false;
	    boolean pasahitzaAurkituta = false;
	    Iterable<Erabiltzailea> erabiltzaileak = erabRepo.findAll();
	    for (Erabiltzailea erabiltzailea : erabiltzaileak) {
	        if (erabiltzailea.getEmail().equals(email)) {
	            emailAurkituta = true;
	            if (erabiltzailea.getPasahitza().equals(pasahitza)) {
	                pasahitzaAurkituta = true;
	            }
	        }
	    }

	    if (emailAurkituta && pasahitzaAurkituta) {
	        System.out.println("bai");
	        return "redirect:/erabiltzaileak/ikusi";  // Redirige si los datos son correctos
	    } else {
	        System.out.println("ez"); // Añade el mensaje de error al modelo
	        return "redirect:/login/login";  // Vuelve a mostrar el formulario de login con el error
	    }
	}
}
