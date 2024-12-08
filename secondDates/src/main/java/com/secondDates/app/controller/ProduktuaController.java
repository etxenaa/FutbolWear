package com.secondDates.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Produktua;
import com.secondDates.app.modelo.Taldea;
import com.secondDates.app.repository.CestaRepository;
import com.secondDates.app.repository.ProduktuaRepository;
import com.secondDates.app.repository.TaldeaRepository;

@Controller
@RequestMapping("/produktua")
public class ProduktuaController {
	@Autowired
	private ProduktuaRepository prodRepo;
	@Autowired
	private TaldeaRepository taldeaRepo;
	@Autowired
	private CestaRepository cestaRepository;

	@GetMapping("/produktuak")
	public String ikusiProduktuak(Model model) {
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
	public String produktuaGehitu(@ModelAttribute("produktua") Produktua producto) {

		prodRepo.save(producto);

		return "redirect:/produktua/produktuak";
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
	public String aldatuProduktua(@ModelAttribute("produktua") Produktua producto) {

		prodRepo.save(producto);

		return "redirect:/produktua/produktuak";
	}

	@GetMapping("/cesta")
	public String ikusiCesta(Model model) {
		Iterable<Cesta> cesta = cestaRepository.findAll();
		model.addAttribute("cestas", cesta);
		return "taulak/cestaTaula";
	}
}