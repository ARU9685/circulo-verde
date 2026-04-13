package com.circuloverde.circulo_verde.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    @GetMapping("/")
    public String mostrarInicio() {
        return "inicio";
        // inicio.html
    }
}

