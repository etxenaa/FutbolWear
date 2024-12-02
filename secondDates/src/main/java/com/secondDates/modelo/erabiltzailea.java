package com.secondDates.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class erabiltzailea {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;         	
	
	@Column(name = "Izena")
    private String izena;    

	@Column(name = "Abizena")
    private String abizena;

	@Column(name = "Email")
    private String emaila;    
	
	@Column(name = "Telefonoa")
	private int tlf;
	
	@Column(name = "Rol")
	private String rol;
	
	
	
	public erabiltzailea() {
		
	}
	
    public erabiltzailea(int id, String izena, String abizena, String emaila) {
        this.id = id;
        this.izena = izena;
        this.abizena = abizena;
        this.emaila = emaila;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getEmaila() {
        return emaila;
    }

    public void setEmaila(String emaila) {
        this.emaila = emaila;
    }
}

