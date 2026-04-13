package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
public class ProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;
    private String categoria;
    private Integer cantidad;   // Cambio: era String, ahora Integer
    private String estado;
}
