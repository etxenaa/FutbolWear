package com.secondDates.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondDates.app.modelo.Erabiltzailea;
import java.util.List;
import java.util.Set;
import com.secondDates.app.modelo.Produktua;


public interface ErabiltzaileaRepository extends JpaRepository<Erabiltzailea, Long> {
	Optional<Erabiltzailea> findByEmail(String email);
	List<Erabiltzailea> findByProduktuak(Produktua produktuak);
}	
