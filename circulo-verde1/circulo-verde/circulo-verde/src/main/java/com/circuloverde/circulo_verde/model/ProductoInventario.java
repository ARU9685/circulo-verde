package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class ProductoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    private String nombre;
    private String categoria;
    private String cantidad;
    private String estado;

    public ProductoInventario() {}

    public ProductoInventario(Long idUsuario, String nombre, String categoria, String cantidad, String estado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    // GETTERS Y SETTERS
    public Long getId() { return id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getCantidad() { return cantidad; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
