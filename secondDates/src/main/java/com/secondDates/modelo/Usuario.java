package com.secondDates.modelo;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;  // ADMIN/USER

    @Embedded
    private Direccion direccion;  // Dirección embebida en Usuario

    @ElementCollection
    @CollectionTable(name = "usuario_telefono", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "telefono")
    private List<String> telefonos;  // Lista de teléfonos del usuario

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas; // Las reservas realizadas por el usuario

    @ManyToMany(mappedBy = "usuarios")
    private List<Mesa> mesas; // Mesas reservadas por el usuario

    // Constructores
    public Usuario() {}

    public Usuario(String nombre, String email, String password, String rol, Direccion direccion, List<String> telefonos) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.direccion = direccion;
        this.telefonos = telefonos;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Mesa> getMesas() {
        return mesas;
    }

    public void setMesas(List<Mesa> mesas) {
        this.mesas = mesas;
    }
}
