package com.secondDates.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int kapazitatea;

    @ManyToMany
    @JoinTable(
        name = "usuario_mesa",
        joinColumns = @JoinColumn(name = "mesa_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios; // Lista de usuarios que han reservado la mesa

    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas; // Las reservas asociadas a esta mesa

    // Constructores
    public Mesa() {}

    public Mesa(int kapazitatea) {
        this.kapazitatea = kapazitatea;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getKapazitatea() {
        return kapazitatea;
    }

    public void setKapazitatea(int kapazitatea) {
        this.kapazitatea = kapazitatea;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
