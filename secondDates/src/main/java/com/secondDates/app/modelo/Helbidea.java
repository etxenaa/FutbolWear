package com.secondDates.app.modelo;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Helbidea {

	private String kalea;  // Calle
	private String postaKodea;  // Código postal
	private String hiriak;  // Ciudad
	private String herrialdea;  // País

	
}
