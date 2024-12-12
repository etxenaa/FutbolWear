package com.secondDates.app.controller;

import java.util.HashSet;

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

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.CestaRepository;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/home")
public class LoginController {

	@Autowired
	private ErabiltzaileaRepository erabRepo;
	@Autowired
	private CestaRepository cestaRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("")
	public String login() {
		return "logeatu/logina";
	}

	@GetMapping("/futbolWear")
	public String index(Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String rol = userDetails.getAuthorities().stream().findFirst().map(auth -> auth.getAuthority()).orElse(null);
		model.addAttribute("rola", rol); // Pasar el rol al modelo
		return "home";
	}

	@GetMapping("/erregistratu")
	public String registratu(Model model) {
		model.addAttribute("erabiltzaileBerria", new Erabiltzailea());
		return "logeatu/register";
	}

	@PostMapping("/erregistratu")
	public String erregistratu(@ModelAttribute("erabiltzaileBerria") Erabiltzailea erab, Model model) {
		if (erabRepo.findByEmail(erab.getEmail()).isPresent()) {
			String errorMessage = "Korreo hori existitzen da.";
			model.addAttribute("error", errorMessage);
			return "logeatu/register";
		}

		erab.setPasahitza(passwordEncoder.encode(erab.getPasahitza()));

		erab.setRola("User");

		erabRepo.save(erab);
		Cesta cesta = new Cesta();
		cesta.setHelbidea(erab.getHelbidea().getPostaKodea() + ", " + erab.getHelbidea().getKalea() + ", "
				+ erab.getHelbidea().getHiria() + ", " + erab.getHelbidea().getHerrialdea());
		cesta.setErabiltzailea(erab);
		cesta.setProduktuak(new HashSet<>()); 
		cestaRepo.save(cesta);

		return "redirect:/home";
	}

}
