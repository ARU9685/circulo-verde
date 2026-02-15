package com.circuloverde.circulo_verde.model;

import jakarta.persistence.*;

@Entity
@Table(name = "diario")
public class EntradaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    private String fecha;
    private String titulo;
    private String contenido;
    private String etiquetas;

    public EntradaDiario() {}

    public EntradaDiario(String fecha, String titulo, String contenido) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.contenido = contenido;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getEtiquetas() { return etiquetas; }
    public void setEtiquetas(String etiquetas) { this.etiquetas = etiquetas; }
}

