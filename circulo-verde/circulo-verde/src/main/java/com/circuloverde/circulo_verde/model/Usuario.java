package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true)
    private String nombre;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasenia;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Introduce un email válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Selecciona tu ciudad")
    private String ciudad;

    private String zonaClimatica;

    private LocalDate fechaRegistro = LocalDate.now();
}


