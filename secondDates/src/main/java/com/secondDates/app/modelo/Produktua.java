package com.secondDates.app.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Produktua {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String izena;  // Nombre
    private String deskribapena;  // Descripción
    private BigDecimal prezioa;  // Precio
    private Integer stock;

    // Relazio N:1 Talde-rekin (produktu bat talde bati dagokio)
    @ManyToOne
    @JoinColumn(name = "taldea_id")
    private Taldea taldea;

    // Nuevo atributo: tamaina (por ejemplo, puede ser una cadena de texto o un valor enum)
    private String tamaina;  // Tamaño del producto

    // Nuevo atributo: irudiaUrl (URL de la imagen del producto)
    private String irudiaUrl;  // URL de la imagen

    // Atributo adicional, por ejemplo, si es un producto destacado o tiene algún tipo de clasificación
    private Boolean nabarmendu;  // Producto destacado (true o false)
    
    // Otros atributos según sea necesario
}

