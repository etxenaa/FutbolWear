package com.secondDates.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secondDates.app.modelo.Erabiltzailea;
import com.secondDates.app.repository.ErabiltzaileaRepository;

@Controller
@RequestMapping("/erabiltzaileak")
public class ErabiltzaileakController {

	@Autowired
	private ErabiltzaileaRepository erabiltzaileaRepository;

	@GetMapping("/ikusi")
	public String ikusiErabiltzaileak(Model model) {
		Iterable<Erabiltzailea> erabiltzaileak = erabiltzaileaRepository.findAll();

		model.addAttribute("usuarios", erabiltzaileak);

		return "taulak/erabiltzaileakTaula"; 
	}
}
