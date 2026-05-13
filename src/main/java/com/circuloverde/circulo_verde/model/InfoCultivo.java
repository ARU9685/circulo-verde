package com.circuloverde.circulo_verde.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Información agrícola completa de un cultivo para una zona climática.
 * No es una entidad JPA — es un objeto de valor usado en el calendario.
 */
@Getter
@AllArgsConstructor
public class InfoCultivo {

    private final String nombre;
    private final String categoria;       // fruto, hoja, raiz, tallo, legumbre, col, tuberculo, otro
    private final String icono;           // emoji representativo

    // Semillero (null si siembra directa)
    private final List<Integer> mesesSemillero;    // meses del año 1-12
    private final Integer diasEnSemillero;         // días que pasa en semillero antes de trasplantar

    // Siembra directa (si aplica)
    private final List<Integer> mesesSiembraDirecta;

    // Plantación / trasplante al exterior
    private final List<Integer> mesesPlantacion;

    // Cosecha
    private final List<Integer> mesesCosecha;

    // Riego
    private final String frecuenciaRiego;          // descripción breve
    private final String tipoRiego;                // goteo, aspersión, surco, mano

    // Mantenimiento
    private final List<String> tareasCuidado;      // lista de cuidados principales

    // Info adicional
    private final String nota;                     // dato especial o consejo clave
}
