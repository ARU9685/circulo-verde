package com.circuloverde.circulo_verde.service;

import org.springframework.stereotype.Service;

@Service
public class ZonaClimaticaService {

    public String obtenerZonaPorCiudad(String ciudad) {

        return switch (ciudad) {
            case "Valencia", "Castellón", "Alicante", "Murcia", "Sevilla", "Málaga","Barcelona", "Tarragona", "Girona", "Almería", "Cádiz", "Huelva",
                 "Granada", "Palma de Mallorca", "Ibiza", "Cartagena", "Elche",
                 "Marbella", "Torrevieja" ->
                    "Mediterránea";

            case "Madrid", "Zaragoza", "Toledo", "Ciudad Real",  "Valladolid", "Burgos", "León", "Salamanca", "Cuenca",
                 "Guadalajara", "Teruel", "Soria", "Albacete", "Palencia",
                 "Segovia", "Ávila" ->
                    "Continental";

            case "Bilbao", "Santander", "A Coruña", "Oviedo",  "San Sebastián", "Vigo", "Pontevedra", "Lugo", "Gijón",
                 "Vitoria-Gasteiz", "Pamplona", "Ferrol", "Ourense" ->
                    "Atlántica";

            case "Las Palmas", "Santa Cruz de Tenerife", "Arrecife", "Puerto del Rosario", "San Cristóbal de la Laguna",
                 "Telde", "Arona", "Adeje", "La Orotava" ->
                    "Subtropical";

            default -> "Mediterránea"; // por defecto
        };
    }
}

