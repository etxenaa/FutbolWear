package com.secondDates.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.CestaRepository;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/saskia")
public class CestaController {

	@Autowired
	private CestaRepository cestaRepo;
	@Autowired
	private ErabiltzaileaRepository erabRepo;

	@GetMapping("/ikusi")
	public String ikusiSaskia(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);

		Optional<Cesta> cestaOpt = cestaRepo.findByErabiltzailea(erabiltzailea);

		if (cestaOpt.isPresent()) {
			model.addAttribute("cesta", cestaOpt.get());
		} else {
			model.addAttribute("cesta", null);
			model.addAttribute("error", "No tienes una cesta activa.");
		}

		return "taulak/cestaTaula";
	}

	@GetMapping("/ezabatu/{id}")
	public String ezabatuSaskia(@PathVariable Long id) {
		Optional<Cesta> cestaOpt = cestaRepo.findById(id);
		if (cestaOpt.isPresent()) {
			Cesta cesta = cestaOpt.get();
			cesta.getProduktuak().clear();
			cestaRepo.save(cesta);
		}

		return "redirect:/produktua/produktuak";
	}

	@GetMapping("/admin/ikusi/{email}")
	public String ikusiSaskiaAdmin(@PathVariable String email, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email2 = auth.getName();
		Erabiltzailea erabiltzailea2 = erabRepo.findByEmail(email2)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea2);
		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		Erabiltzailea erabiltzailea = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		Optional<Cesta> cestaOpt = cestaRepo.findByErabiltzailea(erabiltzailea);

		if (cestaOpt.isPresent()) {
			model.addAttribute("cesta", cestaOpt.get());
		} else {
			return "redirect:/home/futbolWear";
		}

		return "taulak/cestaTaula";
	}

}