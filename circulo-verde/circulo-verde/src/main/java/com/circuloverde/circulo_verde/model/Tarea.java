package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUsuario;

    private LocalDate fecha;

    private String titulo;

    /** riego | siembra | abonado | mantenimiento */
    private String tipo;
}

