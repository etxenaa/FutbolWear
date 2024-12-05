package com.secondDates.app.modelo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 1:1 erlazioa Usuario-rekin
	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Erabiltzailea Erabiltzailea;

	private String helbidea;

	// N:M erlazioa Producto-rekin (taula intermedia produktuak gordeko ditu cestas)
	@ManyToMany
	@JoinTable(name = "cesta_producto", joinColumns = @JoinColumn(name = "cesta_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
	private Set<Produktua> produktuak;

	
}
