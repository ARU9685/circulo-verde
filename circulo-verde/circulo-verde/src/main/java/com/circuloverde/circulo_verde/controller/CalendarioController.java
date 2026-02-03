package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarioController {

    @GetMapping("/calendario")
    public String mostrarCalendario(Model model) {

        // Lista de tareas del mes
        List<Tarea> tareas = new ArrayList<>();
        tareas.add(new Tarea("2024-06-05", "Riego tomates", "riego"));
        tareas.add(new Tarea("2024-06-10", "Siembra lechuga", "siembra"));
        tareas.add(new Tarea("2024-06-15", "Abonar suelo", "abonado"));
        tareas.add(new Tarea("2024-06-20", "Revisión plagas", "mantenimiento"));

        // Enviar lista a la vista
        model.addAttribute("tareas", tareas);

        return "calendario";
    }
}

