package com.secondDates.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondDates.app.modelo.Erabiltzailea;

public interface ErabiltzaileaRepository extends JpaRepository<Erabiltzailea, Long> {
	Optional<Erabiltzailea> findByEmail(String email);
}	
