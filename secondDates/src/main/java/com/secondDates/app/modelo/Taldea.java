package com.secondDates.app.modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Taldea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String izena;  // Nombre
	private String herrialdea;  // País

	// Relazio 1:N Producto-rekin (talde batek hainbat produktuak izan ditzake)
	@OneToMany(mappedBy = "taldea", cascade = CascadeType.ALL)
	private Set<Produktua> produktuak;


}
