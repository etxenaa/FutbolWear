package com.secondDates.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.modelo.Produktua;
import com.secondDates.app.modelo.Taldea;
import com.secondDates.app.repository.CestaRepository;
import com.secondDates.app.repository.ErabiltzaileaRepository;
import com.secondDates.app.repository.ProduktuaRepository;
import com.secondDates.app.repository.TaldeaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
@RequestMapping("/produktua")
public class ProduktuaController {
	@Autowired
	private ProduktuaRepository prodRepo;
	@Autowired
	private TaldeaRepository taldeaRepo;
	@Autowired
	private CestaRepository cestaRepository;
	@Autowired
	private ErabiltzaileaRepository erabRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@GetMapping("/produktuak")
	public String ikusiProduktuak(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		Iterable<Produktua> prod = prodRepo.findAll();
		model.addAttribute("produktuak", prod);
		return "taulak/produktuaTaula";
	}

	@GetMapping("/gehitu")
	public String showAddProductForm(Model model) {
		model.addAttribute("produktua", new Produktua());
		model.addAttribute("taldeak", taldeaRepo.findAll());
		return "/formularioa/produktuaForm";

	}

	@PostMapping("/gehitu")
	public String produktuaGehitu(@ModelAttribute("produktua") Produktua producto,
			@RequestParam("file") MultipartFile irudia, Model model) {
		try {

			if (!irudia.isEmpty()) {
				Path uploadDir = Paths.get("src//main//resources//static/uploads");
				String rutaAbsoluta = uploadDir.toFile().getAbsolutePath();
				byte[] bytesImg = irudia.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + irudia.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				producto.setIrudiaUrl(irudia.getOriginalFilename());
			}

			prodRepo.save(producto);

			return "redirect:/produktua/produktuak";

		} catch (IOException e) {

			e.printStackTrace();

			return "error"; // Si ocurre un error, mostramos una página de error

		}
	}

	@PostMapping("/taldea/gehitu")
	public String taldeaGehitu(@ModelAttribute("taldea") Taldea taldea) {

		taldeaRepo.save(taldea);

		return "redirect:/produktua/gehitu";
	}

	@GetMapping("/editatu/{id}")
	public String aldatuProd(@PathVariable Long id, Model model) {
		Optional<Produktua> prod = prodRepo.findById(id);
		List<Taldea> taldeak = taldeaRepo.findAll();
		if (prod.isPresent()) {
			model.addAttribute("produktua", prod.get());
			model.addAttribute("taldeak", taldeak);
		} else {
			return "error/notFound";
		}
		return "formularioa/aldatuProd";
	}

	@GetMapping("/ezabatu/{id}")
	public String eliminarProducto(@PathVariable Long id) {
		if (prodRepo.existsById(id)) {
			prodRepo.deleteById(id);
		} else {

			System.out.println("Producto no encontrado con id: " + id);
		}

		return "redirect:/produktua/produktuak";
	}

	@PostMapping("/editatu/{id}")
	public String aldatuProduktua(@ModelAttribute("produktua") Produktua producto,
			@RequestParam("file") MultipartFile irudia, Model model) {
		try {
			if (!irudia.isEmpty()) {
				Path uploadDir = Paths.get("src/main/resources/static/uploads");
				String rutaAbsoluta = uploadDir.toFile().getAbsolutePath();
				byte[] bytesImg = irudia.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + irudia.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				producto.setIrudiaUrl(irudia.getOriginalFilename());
			}

			// Usar save() de JpaRepository, que maneja la transacción
			prodRepo.save(producto);

			return "redirect:/produktua/produktuak"; // Redirigir después de guardar
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error"; // Página de error
		}
	}

	
}
