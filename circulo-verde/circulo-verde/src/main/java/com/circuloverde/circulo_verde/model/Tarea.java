package com.circuloverde.circulo_verde.model;

public class Tarea {

    private String fecha;     // formato: "2024-06-10"
    private String titulo;    // ejemplo: "Siembra de lechuga"
    private String tipo;      // ejemplo: "siembra", "riego", etc.

    public Tarea(String fecha, String titulo, String tipo) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }
}


