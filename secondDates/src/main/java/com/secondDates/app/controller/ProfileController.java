package com.secondDates.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.ErabiltzaileaRepository;
import com.secondDates.app.security.CustomUserDetailsService;

@Controller
@RequestMapping("/perfil")
public class ProfileController {

	@Autowired
	private CustomUserDetailsService userService;

	@Autowired
	private ErabiltzaileaRepository erabRepo;

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
	public String editarPerfil(@ModelAttribute("erabiltzailea") Erabiltzailea usuarioFormulario) {
		// Obtener el usuario actual desde la base de datos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea usuarioActual = erabRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		usuarioActual.setIzena(usuarioFormulario.getIzena());
		usuarioActual.setEmail(usuarioFormulario.getEmail());
		usuarioActual.setHelbidea(usuarioFormulario.getHelbidea());
		usuarioActual.setTelefonoa(usuarioFormulario.getTelefonoa());
		
		erabRepo.save(usuarioActual);

		return "redirect:/perfil/ver"; // Redirigir a la página de perfil
	}

}
