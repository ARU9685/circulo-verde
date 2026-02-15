package com.circuloverde.circulo_verde.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CalendarioSiembraService {

    public List<String> obtenerSiembrasDelMes(String zona) {
        int mes = LocalDate.now().getMonthValue();
        return obtenerSiembrasDelMes(zona, mes);
    }

    public List<String> obtenerSiembrasDelMes(String zona, int mes) {

        return switch (zona) {

            case "Mediterránea" ->
                    siembrasMediterranea().getOrDefault(mes, List.of());

            case "Continental" ->
                    siembrasContinental().getOrDefault(mes, List.of());

            case "Atlántica" ->
                    siembrasAtlantica().getOrDefault(mes, List.of());

            case "Subtropical" ->
                    siembrasSubtropical().getOrDefault(mes, List.of());

            default -> List.of();
        };
    }

    private Map<Integer, List<String>> siembrasMediterranea() {
        return Map.of(
                1, List.of("Lechuga", "Cebolla", "Ajo"),
                2, List.of("Tomate", "Pimiento", "Berenjena"),
                3, List.of("Calabacín", "Pepino", "Melón"),
                4, List.of("Albahaca", "Judías verdes"),
                5, List.of("Sandía", "Calabaza"),
                9, List.of("Espinaca", "Acelga"),
                10, List.of("Rábanos", "Zanahoria")
        );
    }

    private Map<Integer, List<String>> siembrasContinental() {
        return Map.of(
                3, List.of("Patata", "Zanahoria", "Cebolla"),
                4, List.of("Col", "Lechuga", "Ajo"),
                5, List.of("Tomate", "Pimiento"),
                6, List.of("Calabacín", "Pepino"),
                9, List.of("Espinaca", "Acelga")
        );
    }

    private Map<Integer, List<String>> siembrasAtlantica() {
        return Map.of(
                2, List.of("Guisantes", "Habitas"),
                3, List.of("Lechuga", "Repollo"),
                4, List.of("Puerro", "Remolacha"),
                5, List.of("Fresas", "Acelga"),
                9, List.of("Espinaca", "Rábanos")
        );
    }

    private Map<Integer, List<String>> siembrasSubtropical() {
        return Map.of(
                1, List.of("Batata", "Tomate"),
                2, List.of("Pepino", "Calabacín"),
                3, List.of("Aguacate", "Papaya"),
                4, List.of("Mango", "Plátano"),
                10, List.of("Lechuga", "Espinaca")
        );
    }
}


