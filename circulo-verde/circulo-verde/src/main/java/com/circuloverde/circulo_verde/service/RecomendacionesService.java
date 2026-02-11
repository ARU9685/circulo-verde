package com.circuloverde.circulo_verde.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecomendacionesService {

    public List<String> obtenerCultivos(String zona) {

        return switch (zona) {
            case "Mediterránea" -> List.of(
                    "Tomate",
                    "Pimiento",
                    "Berenjena",
                    "Calabacín",
                    "Lechuga",
                    "Albahaca"
            );

            case "Continental" -> List.of(
                    "Zanahoria",
                    "Patata",
                    "Col",
                    "Ajo",
                    "Cebolla",
                    "Espinaca"
            );

            case "Atlántica" -> List.of(
                    "Repollo",
                    "Acelga",
                    "Guisantes",
                    "Fresas",
                    "Puerro",
                    "Remolacha"
            );

            case "Subtropical" -> List.of(
                    "Aguacate",
                    "Mango",
                    "Papaya",
                    "Batata",
                    "Plátano",
                    "Maracuyá"
            );

            default -> List.of("Lechuga", "Zanahoria");
        };
    }

    public String obtenerConsejo(String zona) {

        return switch (zona) {
            case "Mediterránea" ->
                    "Clima cálido y seco: riega al amanecer y protege los cultivos del sol fuerte.";

            case "Continental" ->
                    "Clima con inviernos fríos: aprovecha la primavera para siembras tempranas.";

            case "Atlántica" ->
                    "Clima húmedo: vigila hongos y mejora el drenaje del suelo.";

            case "Subtropical" ->
                    "Clima cálido todo el año: ideal para cultivos exóticos y tropicales.";

            default -> "Cuida el suelo y mantén un riego equilibrado.";
        };
    }
}

