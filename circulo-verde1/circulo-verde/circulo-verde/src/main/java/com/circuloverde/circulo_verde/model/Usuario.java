package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true)
    private String nombre;

    private String contrasenia;

    @Email(message = "Correo inválido")
    @Column(unique = true)
    private String email;

    private String ciudad;
    private String zonaClimatica;

    private LocalDate fechaRegistro = LocalDate.now();

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getZonaClimatica() { return zonaClimatica; }
    public void setZonaClimatica(String zonaClimatica) { this.zonaClimatica = zonaClimatica; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}


