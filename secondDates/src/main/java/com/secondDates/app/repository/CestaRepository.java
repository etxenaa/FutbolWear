package com.secondDates.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secondDates.app.modelo.Cesta;
import com.secondDates.app.modelo.Erabiltzailea;

public interface CestaRepository extends JpaRepository<Cesta, Long> {
	Optional<Cesta> findByErabiltzailea(Erabiltzailea erabiltzailea);

}
