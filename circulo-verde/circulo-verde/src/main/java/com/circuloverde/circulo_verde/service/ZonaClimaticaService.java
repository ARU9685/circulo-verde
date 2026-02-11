package com.circuloverde.circulo_verde.service;

import org.springframework.stereotype.Service;

@Service
public class ZonaClimaticaService {

    public String obtenerZonaPorCiudad(String ciudad) {

        return switch (ciudad) {
            case "Valencia", "Castellón", "Alicante", "Murcia", "Sevilla", "Málaga" ->
                    "Mediterránea";

            case "Madrid", "Zaragoza", "Toledo", "Ciudad Real" ->
                    "Continental";

            case "Bilbao", "Santander", "A Coruña", "Oviedo" ->
                    "Atlántica";

            case "Las Palmas", "Santa Cruz de Tenerife" ->
                    "Subtropical";

            default -> "Mediterránea"; // por defecto
        };
    }
}

