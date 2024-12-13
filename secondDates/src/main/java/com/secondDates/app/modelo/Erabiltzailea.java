	package com.secondDates.app.modelo;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.JoinColumn;

@Entity
@Getter
@Setter
public class Erabiltzailea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String izena;
	private String email;
	private String pasahitza;
	private String telefonoa;
	private String rola; 

	@Embedded
	private Helbidea helbidea; 

	@OneToOne(mappedBy = "erabiltzailea", cascade = CascadeType.ALL)
	private Cesta cesta;

	@ManyToMany
	@JoinTable(name = "usuario_producto", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "producto_id"))
	private Set<Produktua> produktuak;
	
	
}
