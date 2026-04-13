package com.circuloverde.circulo_verde.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CalendarioSiembraService {

    public List<String> obtenerSiembrasDelMes(String zona) {
        return obtenerSiembrasDelMes(zona, LocalDate.now().getMonthValue());
    }

    public List<String> obtenerSiembrasDelMes(String zona, int mes) {
        return switch (zona) {
            case "Mediterránea" -> siembrasMediterranea().getOrDefault(mes, List.of());
            case "Continental"  -> siembrasContinental().getOrDefault(mes, List.of());
            case "Atlántica"    -> siembrasAtlantica().getOrDefault(mes, List.of());
            case "Subtropical"  -> siembrasSubtropical().getOrDefault(mes, List.of());
            default             -> List.of();
        };
    }

    private Map<Integer, List<String>> siembrasMediterranea() {
        return Map.ofEntries(
                Map.entry(1,  List.of("Lechuga", "Cebolla", "Ajo", "Acelga", "Espinaca", "Rábano")),
                Map.entry(2,  List.of("Tomate", "Pimiento", "Berenjena", "Lechuga", "Cebolla")),
                Map.entry(3,  List.of("Calabacín", "Pepino", "Melón", "Sandía", "Patata", "Zanahoria")),
                Map.entry(4,  List.of("Albahaca", "Judías verdes", "Calabaza", "Pepino")),
                Map.entry(5,  List.of("Sandía", "Calabaza", "Pepino", "Melón")),
                Map.entry(6,  List.of("Tomate", "Pimiento", "Berenjena", "Judías verdes")),
                Map.entry(7,  List.of("Tomate", "Judías verdes", "Pepino")),
                Map.entry(8,  List.of("Acelga", "Espinaca", "Lechuga", "Rábano")),
                Map.entry(9,  List.of("Espinaca", "Acelga", "Zanahoria", "Cebolla")),
                Map.entry(10, List.of("Rábano", "Zanahoria", "Cebolla", "Ajo")),
                Map.entry(11, List.of("Ajo", "Lechuga", "Espinaca")),
                Map.entry(12, List.of("Acelga", "Espinaca", "Lechuga", "Rábano"))
        );
    }

    private Map<Integer, List<String>> siembrasContinental() {
        return Map.ofEntries(
                Map.entry(1,  List.of("Ajo", "Cebolla")),
                Map.entry(2,  List.of("Lechuga", "Espinaca", "Acelga")),
                Map.entry(3,  List.of("Patata", "Zanahoria", "Cebolla", "Guisantes")),
                Map.entry(4,  List.of("Col", "Lechuga", "Ajo", "Remolacha", "Puerro")),
                Map.entry(5,  List.of("Tomate", "Pimiento", "Berenjena", "Calabacín")),
                Map.entry(6,  List.of("Calabacín", "Pepino", "Judías verdes", "Calabaza")),
                Map.entry(7,  List.of("Calabaza", "Pepino", "Judías verdes")),
                Map.entry(8,  List.of("Acelga", "Espinaca", "Lechuga")),
                Map.entry(9,  List.of("Espinaca", "Acelga", "Rábano", "Zanahoria")),
                Map.entry(10, List.of("Ajo", "Cebolla", "Lechuga")),
                Map.entry(11, List.of("Lechuga", "Espinaca")),
                Map.entry(12, List.of("Ajo", "Cebolla"))
        );
    }

    private Map<Integer, List<String>> siembrasAtlantica() {
        return Map.ofEntries(
                Map.entry(1,  List.of("Acelga", "Espinaca", "Lechuga")),
                Map.entry(2,  List.of("Guisantes", "Habas", "Zanahoria")),
                Map.entry(3,  List.of("Lechuga", "Repollo", "Cebolla")),
                Map.entry(4,  List.of("Puerro", "Remolacha", "Zanahoria", "Acelga")),
                Map.entry(5,  List.of("Fresas", "Acelga", "Lechuga", "Judías verdes")),
                Map.entry(6,  List.of("Judías verdes", "Calabacín", "Pepino")),
                Map.entry(7,  List.of("Pepino", "Calabaza", "Lechuga")),
                Map.entry(8,  List.of("Acelga", "Espinaca", "Rábano")),
                Map.entry(9,  List.of("Espinaca", "Rábano", "Zanahoria", "Cebolla")),
                Map.entry(10, List.of("Ajo", "Cebolla", "Lechuga")),
                Map.entry(11, List.of("Lechuga", "Espinaca", "Acelga")),
                Map.entry(12, List.of("Acelga", "Espinaca", "Lechuga"))
        );
    }

    private Map<Integer, List<String>> siembrasSubtropical() {
        return Map.ofEntries(
                Map.entry(1,  List.of("Batata", "Tomate", "Pepino", "Lechuga")),
                Map.entry(2,  List.of("Pepino", "Calabacín", "Tomate", "Pimiento")),
                Map.entry(3,  List.of("Aguacate", "Papaya", "Melón", "Sandía")),
                Map.entry(4,  List.of("Mango", "Plátano", "Sandía", "Calabaza")),
                Map.entry(5,  List.of("Calabaza", "Pepino", "Melón")),
                Map.entry(6,  List.of("Tomate", "Pimiento", "Berenjena")),
                Map.entry(7,  List.of("Judías verdes", "Calabacín", "Pepino")),
                Map.entry(8,  List.of("Lechuga", "Espinaca", "Acelga")),
                Map.entry(9,  List.of("Acelga", "Zanahoria", "Rábano")),
                Map.entry(10, List.of("Lechuga", "Espinaca", "Cebolla")),
                Map.entry(11, List.of("Ajo", "Cebolla", "Lechuga")),
                Map.entry(12, List.of("Acelga", "Espinaca", "Lechuga"))
        );
    }

    public List<String> ajustarSiembrasPorClima(List<String> siembras, Map<String, String> clima) {
        // Variables finales obligatorias para usarlas dentro del lambda
        final double temp   = parsearDouble(clima.getOrDefault("temperatura", "20"), 20.0);
        final double viento = parsearDouble(clima.getOrDefault("viento", "0"), 0.0);

        final List<String> cultivosVerano = List.of("tomate", "pimiento", "berenjena", "calabacín", "pepino", "melón", "sandía");
        final List<String> cultivosHoja   = List.of("lechuga", "espinaca", "acelga");

        return siembras.stream()
                .filter(c -> {
                    String nombre = c.toLowerCase();
                    if (temp   < 10 && cultivosVerano.stream().anyMatch(nombre::contains)) return false;
                    if (temp   > 30 && cultivosHoja.stream().anyMatch(nombre::contains))   return false;
                    if (viento > 10 && nombre.contains("trasplante"))                       return false;
                    return true;
                })
                .toList();
    }

    private double parsearDouble(String valor, double defecto) {
        try {
            return Double.parseDouble(valor.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return defecto;
        }
    }
}


