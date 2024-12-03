package com.secondDates.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaEntrada; // Fecha y hora de la reserva

    @Column(nullable = false)
    private int numPersonas; // Número de personas para la reserva, puede ser 1 o 2

    @Column(nullable = false)
    private String estado; // Estado de la reserva (Pendiente, Confirmada, Cancelada)

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // El usuario que hace la reserva

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa; // La mesa reservada

    // Constructores
    public Reserva() {}

    public Reserva(LocalDateTime fechaEntrada, int numPersonas, String estado, Usuario usuario, Mesa mesa) {
        this.fechaEntrada = fechaEntrada;
        this.numPersonas = numPersonas;
        this.estado = estado;
        this.usuario = usuario;
        this.mesa = mesa;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
}

