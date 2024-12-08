package com.secondDates.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.secondDates.app.modelo.Produktua;
import java.util.List;
import java.util.Optional;


public interface ProduktuaRepository extends JpaRepository<Produktua, Long> {
	Optional findById(Long id);
} 
