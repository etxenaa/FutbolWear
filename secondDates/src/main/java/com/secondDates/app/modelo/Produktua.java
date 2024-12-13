	package com.secondDates.app.modelo;

import java.math.BigDecimal;
import java.util.List;

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
    
    private String izena; 
    private String deskribapena;  
    private BigDecimal prezioa;  
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "taldea_id")
    private Taldea taldea;

    private String tamaina;  
    private List<String> tamainaList;
    private String irudiaUrl;

}

