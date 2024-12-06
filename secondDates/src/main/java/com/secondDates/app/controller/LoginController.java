package com.secondDates.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.modelo.Produktua;
import com.secondDates.app.repository.ErabiltzaileaRepository;
import com.secondDates.app.repository.ProduktuaRepository;

@Controller
@RequestMapping("/")
public class LoginController {  
	
	@Autowired
    private ErabiltzaileaRepository erabiltzaileaRepository;

	@Autowired
    private ProduktuaRepository produktuaRepository;
	
    @GetMapping("/erregistratu")
    public String registratu() {
    	return "logeatu/register";
    }
    
    @GetMapping("/erabiltzaileak")  // Ruta para ver todos los usuarios
    public String ikusiErabiltzaileak(Model model) {
        // Obtener todos los usuarios
        Iterable<Erabiltzailea> erabiltzaileak = erabiltzaileaRepository.findAll();
        
        // Pasar los usuarios al modelo
        model.addAttribute("usuarios", erabiltzaileak);
        
        // Retornar la vista donde se mostrarán los usuarios
        return "taulak/erabiltzaileakTaula";  // Aquí debes tener un archivo usuarios.html
    }
    
    @GetMapping("/produktuak")
    public String ikusiProduktuak(Model model) {
        Iterable<Produktua> prod = produktuaRepository.findAll();
        model.addAttribute("productos", prod);  // Cambié "produktua" a "productos"
        return "taulak/produktuaTaula";
    }
}
