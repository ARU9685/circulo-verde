package com.circuloverde.circulo_verde.model;

public class EntradaDiario {

    private String fecha;
    private String titulo;
    private String contenido;

    public EntradaDiario(String fecha, String titulo, String contenido) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }
}

