package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.CalendarioSiembraService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarioController {

    private final CalendarioSiembraService calendarioSiembraService;

    public CalendarioController(CalendarioSiembraService calendarioSiembraService) {
        this.calendarioSiembraService = calendarioSiembraService;
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(
            @RequestParam(required = false) Integer mes,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        // Si no se pasa mes, usamos el actual
        if (mes == null) {
            mes = LocalDate.now().getMonthValue();
        }

        String zona = usuario.getZonaClimatica();

        //Obtener siembras recomendadas para este mes y zona
        List<String> siembrasMes = calendarioSiembraService.obtenerSiembrasDelMes(zona, mes);

        // Tareas del usuario
        List<Tarea> tareas = new ArrayList<>();
        tareas.add(new Tarea("2024-06-05", "Riego tomates", "riego"));
        tareas.add(new Tarea("2024-06-10", "Siembra lechuga", "siembra"));
        tareas.add(new Tarea("2024-06-15", "Abonar suelo", "abonado"));
        tareas.add(new Tarea("2024-06-20", "Revisión plagas", "mantenimiento"));

        // Enviar datos a la vista
        model.addAttribute("tareas", tareas);
        model.addAttribute("siembrasMes", siembrasMes);
        model.addAttribute("zona", zona);
        model.addAttribute("mes", mes);

        return "calendario";
    }
}

