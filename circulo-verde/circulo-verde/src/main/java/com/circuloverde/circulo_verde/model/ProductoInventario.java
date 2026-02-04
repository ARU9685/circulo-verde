package com.circuloverde.circulo_verde.model;

public class ProductoInventario {

    private String nombre;
    private String categoria;
    private String cantidad;
    private String estado;

    public ProductoInventario(String nombre, String categoria, String cantidad, String estado) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getCantidad() {
        return cantidad;
    }

    public String getEstado() {
        return estado;
    }
}

