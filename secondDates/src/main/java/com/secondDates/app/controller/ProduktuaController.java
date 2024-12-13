package com.secondDates.app.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
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

	@GetMapping("/admin/produktuak")
	public String ikusiProduktuak(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		Iterable<Produktua> prod = prodRepo.findAll();
		if (prodRepo.findAll().isEmpty()) {
			model.addAttribute("produktuak", null);
		} else {
			for (Produktua produktua : prod) {
				if (produktua.getTamaina() != null) {
					produktua.setTamainaList(Arrays.asList(produktua.getTamaina().split(",")));
				}
			}
			model.addAttribute("produktuak", prod);
		}

		return "taulak/produktuaTaula";
	}

	@GetMapping("/admin/gehitu")
	public String showAddProductForm(Model model) {
		model.addAttribute("produktua", new Produktua());
		model.addAttribute("taldeak", taldeaRepo.findAll());
		return "/formularioa/produktuaForm";

	}

	@PostMapping("/admin/gehitu")
	public String produktuaGehitu(@ModelAttribute("produktua") Produktua producto,
			@RequestParam("file") MultipartFile irudia, Model model) {
		boolean produktuaExistitzenDa = false;
		try {

			if (!irudia.isEmpty()) {
				Path uploadDir = Paths.get("src//main//resources//static/uploads");
				String rutaAbsoluta = uploadDir.toFile().getAbsolutePath();
				byte[] bytesImg = irudia.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + irudia.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				producto.setIrudiaUrl(irudia.getOriginalFilename());
			}
			List<Produktua> produktuLista = prodRepo.findAll();
			for (Produktua produktua : produktuLista) {
				if (produktua.getIzena().equals(producto.getIzena())) {
					model.addAttribute("error", producto.getIzena());
					produktuaExistitzenDa = true;
				}
			}

			if (!produktuaExistitzenDa) {
				prodRepo.save(producto);
				return "redirect:/produktua/admin/produktuak";
			} else {
				return "formularioa/produktuaForm.html";
			}

		} catch (IOException e) {

			e.printStackTrace();

			return "error"; // Si ocurre un error, mostramos una página de error

		}
	}

	@GetMapping("/admin/ezabatu/{id}")
	public String eliminarProducto(@PathVariable Long id) {
		Optional<Produktua> produktoa = prodRepo.findById(id);

		if (produktoa.isPresent()) {
			Produktua produktua = produktoa.get();

			List<Cesta> cestas = cestaRepository.findByProduktuak(produktua);
			for (Cesta cesta : cestas) {
				cesta.getProduktuak().remove(produktua);

				cestaRepository.save(cesta);
			}
			List<Erabiltzailea> erab = erabRepository.findByProduktuak(produktua);
			for (Erabiltzailea erabiltzailea : erab) {
				erabiltzailea.getProduktuak().remove(produktua);
				erabRepository.save(erabiltzailea);
			}
			prodRepo.delete(produktua);
		} else {
			System.out.println("Producto no encontrado con id: " + id);
		}

		return "redirect:/produktua/admin/produktuak";
	}

	@GetMapping("/admin/editatu/{id}")
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

	@PostMapping("/admin/editatu/{id}")
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

			return "redirect:/produktua/admin/produktuak"; // Redirigir después de guardar
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", e.getMessage());
			return "error"; // Página de error
		}
	}

	@GetMapping("/produktuak")
	public String ikusiProduktuakUser(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		Iterable<Produktua> prod = prodRepo.findAll();
		if (prodRepo.findAll().isEmpty()) {
			model.addAttribute("produktuak", null);
		} else {
			for (Produktua produktua : prod) {
				if (produktua.getTamaina() != null) {
					produktua.setTamainaList(Arrays.asList(produktua.getTamaina().split(",")));
				}
			}
			model.addAttribute("produktuak", prod);
		}
		return "taulak/produktuaTaula";
	}

	@GetMapping("/produktuak/erosi/{id}")
	public String produktuaErosiUser(@PathVariable Long id,
			Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		Optional<Cesta> cestaGuztiak = cestaRepository.findByErabiltzailea(erabiltzailea);

		Cesta cesta = cestaGuztiak.get();

		if (cesta.getProduktuak() == null) {
			cesta.setProduktuak(new HashSet<>());
		}
		Optional<Produktua> producto = prodRepo.findById(id);
		System.out.println(producto.get().getTamaina());
		cesta.getProduktuak().add(producto.get());
		cestaRepository.save(cesta);
		erabiltzailea.getProduktuak().add(producto.get());
		erabRepository.save(erabiltzailea);
		return "redirect:/produktua/produktuak";
	}

	@GetMapping("/admin/taldea/gehitu")
	public String taldeaGehitu(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		model.addAttribute("taldea", new Taldea());
		return "formularioa/taldeaGehitu.html";
	}

	@PostMapping("/admin/taldea/gehitu")
	public String taldeagehituta(@ModelAttribute("taldea") Taldea taldea, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		Erabiltzailea erabiltzailea = erabRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		model.addAttribute("erabiltzailea", erabiltzailea);

		String rol = auth.getAuthorities().stream().map(authority -> authority.getAuthority()).findFirst().orElse(null);
		model.addAttribute("rola", rol);
		boolean taldeaExistitzenDa = false;
		List<Taldea> taldeaLista = taldeaRepo.findAll();
		for (Taldea taldea2 : taldeaLista) {
			if (taldea2.getIzena().equals(taldea.getIzena())) {
				model.addAttribute("error", taldea.getIzena());
				taldeaExistitzenDa = true;
			}
		}

		if (!taldeaExistitzenDa) {
			taldeaRepo.save(taldea);
			return "redirect:/produktua/admin/produktuak";
		} else {
			return "formularioa/taldeaGehitu.html";
		}

	}
}
