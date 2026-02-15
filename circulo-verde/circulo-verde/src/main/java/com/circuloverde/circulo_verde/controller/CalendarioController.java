package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.CalendarioSiembraService;

import com.circuloverde.circulo_verde.service.TareaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarioController {

    private final CalendarioSiembraService calendarioSiembraService;
    private final TareaService tareaService;

    public CalendarioController(CalendarioSiembraService calendarioSiembraService,
                                TareaService tareaService) {
        this.calendarioSiembraService = calendarioSiembraService;
        this.tareaService = tareaService;
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer año,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        LocalDate hoy = LocalDate.now();
        if (mes == null) mes = hoy.getMonthValue();
        if (año == null) año = hoy.getYear();

        String zona = usuario.getZonaClimatica();

        // Siembras recomendadas para este mes
        List<String> siembrasMes = calendarioSiembraService.obtenerSiembrasDelMes(zona, mes);

        // Generar calendario
        YearMonth ym = YearMonth.of(año, mes);

        //nombres mes y año en español
        String nombreMes = ym.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL,
                new java.util.Locale("es")
        );
        model.addAttribute("nombreMes", nombreMes);

        int diasMes = ym.lengthOfMonth();
        LocalDate primerDia = ym.atDay(1);
        int diaSemanaInicio = primerDia.getDayOfWeek().getValue(); // 1=Lunes

        List<List<Integer>> semanas = new ArrayList<>();
        List<Integer> semanaActual = new ArrayList<>();

        // Huecos antes del día 1
        for (int i = 1; i < diaSemanaInicio; i++) {
            semanaActual.add(null);
        }

        // Días del mes
        for (int dia = 1; dia <= diasMes; dia++) {
            semanaActual.add(dia);

            if (semanaActual.size() == 7) {
                semanas.add(semanaActual);
                semanaActual = new ArrayList<>();
            }
        }

        // Huecos finales
        if (!semanaActual.isEmpty()) {
            while (semanaActual.size() < 7) semanaActual.add(null);
            semanas.add(semanaActual);
        }

        // tareas
        List<Tarea> tareas = tareaService.obtenerTareasDelMes(año, mes);
        model.addAttribute("tareas", tareas);


        model.addAttribute("tareas", tareas);
        model.addAttribute("siembrasMes", siembrasMes);
        model.addAttribute("zona", zona);
        model.addAttribute("mes", mes);
        model.addAttribute("año", año);
        model.addAttribute("semanas", semanas);

        return "calendario";
    }
}

