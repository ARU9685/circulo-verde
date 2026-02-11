package com.circuloverde.circulo_verde.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EstadisticasController {

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {

        model.addAttribute("tareasCompletadas", 12);
        model.addAttribute("productosInventario", 8);
        model.addAttribute("entradasDiario", 5);

        return "estadisticas";
    }
}

