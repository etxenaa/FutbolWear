package com.secondDates.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.CestaRepository;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/perfil")
public class ProfileController {

	@Autowired
	private ErabiltzaileaRepository erabRepo;
	@Autowired
	private CestaRepository cestaRepo;

	// Mostrar perfil
	@GetMapping("/ver")
	public String mostrarPerfil(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName(); // Obtener el email del usuario autenticado

		Erabiltzailea erabiltzailea = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		return "perfil";
	}

	@GetMapping("/editar")
	public String editarPerfil(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea usuario = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		model.addAttribute("erabiltzailea", usuario);
		return "formularioa/aldatuPerfil";
	}

	@PostMapping("/editar")
	public String editarPerfil(@ModelAttribute("erabiltzailea") Erabiltzailea usuarioFormulario,
			@RequestParam("telefonoa") String nuevoTelefono) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea usuarioActual = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		usuarioActual.setIzena(usuarioFormulario.getIzena());
		usuarioActual.setEmail(usuarioFormulario.getEmail());
		usuarioActual.setHelbidea(usuarioFormulario.getHelbidea());
		System.out.println("Usuario actual: " + usuarioActual.getTelefono_zbk());
		System.out.println("Usuario form: " + usuarioFormulario.getTelefono_zbk());
		if (nuevoTelefono != null && !nuevoTelefono.isEmpty()) {
			usuarioActual.getTelefono_zbk().add(nuevoTelefono);
		}

		erabRepo.save(usuarioActual);

		return "redirect:/perfil/ver";
	}

	@GetMapping("/ezabatu")
	public String kontuaEzabatu() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea usuarioActual = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		Optional<Cesta> cesta = cestaRepo.findByErabiltzailea(usuarioActual);

		usuarioActual.getProduktuak().clear();
		usuarioActual.getTelefono_zbk().clear();
		cesta.get().getProduktuak().clear();
		erabRepo.delete(usuarioActual);
		cestaRepo.deleteById(cesta.get().getId());
		return "redirect:/home";
	}

	@GetMapping("/admin/ezabatu/{id}")
	public String ezabatuErabAdmin(@PathVariable Long id) {
		Optional<Erabiltzailea> erab = erabRepo.findById(id);
		Erabiltzailea erabiltzailea = erab.get();
		Optional<Cesta> cesta = cestaRepo.findByErabiltzailea(erabiltzailea);
		erabiltzailea.getProduktuak().clear();
		erabiltzailea.getTelefono_zbk().clear();
		cesta.get().getProduktuak().clear();
		erabRepo.delete(erabiltzailea);
		cestaRepo.deleteById(cesta.get().getId());
		return "redirect:/erabiltzaileak/ikusi";
	}
}
