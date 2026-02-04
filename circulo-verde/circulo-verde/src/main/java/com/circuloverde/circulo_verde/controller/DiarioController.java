package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.EntradaDiario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DiarioController {

    @GetMapping("/diario")
    public String mostrarDiario(Model model) {

        // Entradas simuladas del diario
        List<EntradaDiario> entradas = List.of(
                new EntradaDiario(
                        "2024-06-03",
                        "Primeras flores en los tomates",
                        "Hoy he visto las primeras flores en las tomateras. El riego y el sol están funcionando muy bien."
                ),
                new EntradaDiario(
                        "2024-05-28",
                        "Trasplante de lechugas",
                        "He trasplantado las lechugas al bancal principal. El suelo estaba perfecto después de la lluvia."
                ),
                new EntradaDiario(
                        "2024-05-20",
                        "Preparación del terreno",
                        "He añadido compost y removido la tierra. Todo listo para las nuevas siembras."
                )
        );

        model.addAttribute("entradas", entradas);

        return "diario"; // carga diario.html
    }
}

