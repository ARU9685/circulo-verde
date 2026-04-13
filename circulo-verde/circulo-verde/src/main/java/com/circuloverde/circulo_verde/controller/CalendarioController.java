package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.CalendarioSiembraService;
import com.circuloverde.circulo_verde.service.SativumService;
import com.circuloverde.circulo_verde.service.TareaService;
import com.circuloverde.circulo_verde.service.WeatherService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Controller
public class CalendarioController {

    private final CalendarioSiembraService calendarioSiembraService;
    private final TareaService tareaService;
    private final SativumService sativumService;
    private final WeatherService weatherService;

    public CalendarioController(CalendarioSiembraService calendarioSiembraService,
                                TareaService tareaService,
                                SativumService sativumService,
                                WeatherService weatherService) {
        this.calendarioSiembraService = calendarioSiembraService;
        this.tareaService = tareaService;
        this.sativumService = sativumService;
        this.weatherService = weatherService;
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio,   // «año» causaba encoding issues en algunos servidores
            Authentication auth,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/login";

        LocalDate hoy = LocalDate.now();
        int mesActual  = (mes  != null) ? mes  : hoy.getMonthValue();
        int anioActual = (anio != null) ? anio : hoy.getYear();

        // Corregir desbordamiento de mes (< 1 o > 12)
        if (mesActual < 1) { mesActual = 12; anioActual--; }
        if (mesActual > 12) { mesActual = 1; anioActual++; }

        YearMonth ym = YearMonth.of(anioActual, mesActual);

        String nombreMes = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
        // Capitalizar primera letra
        nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);

        // --- Calendario: semanas ---
        int diasMes = ym.lengthOfMonth();
        int diaSemanaInicio = ym.atDay(1).getDayOfWeek().getValue(); // 1=Lun

        List<List<Integer>> semanas = new ArrayList<>();
        List<Integer> semanaActual = new ArrayList<>();

        for (int i = 1; i < diaSemanaInicio; i++) semanaActual.add(null);

        for (int dia = 1; dia <= diasMes; dia++) {
            semanaActual.add(dia);
            if (semanaActual.size() == 7) {
                semanas.add(semanaActual);
                semanaActual = new ArrayList<>();
            }
        }
        if (!semanaActual.isEmpty()) {
            while (semanaActual.size() < 7) semanaActual.add(null);
            semanas.add(semanaActual);
        }

        // --- Tareas del mes (solo del usuario) ---
        List<Tarea> tareas = tareaService.obtenerTareasDelMes(usuario.getId(), anioActual, mesActual);

        // --- Siembras recomendadas ---
        String zona = usuario.getZonaClimatica();
        List<String> siembrasMes = calendarioSiembraService.obtenerSiembrasDelMes(zona, mesActual);

        // --- Clima ---
        Map<String, String> clima = weatherService.obtenerTiempo(usuario.getCiudad());
        List<String> siembrasAjustadas = calendarioSiembraService.ajustarSiembrasPorClima(siembrasMes, clima);

        // --- Sativum: plagas y fertilizantes ---
        List<JsonNode> plagas = new ArrayList<>();
        JsonNode plagasJson = sativumService.get("/pests?crop=1");
        if (plagasJson != null && plagasJson.isArray()) plagasJson.forEach(plagas::add);

        List<JsonNode> fertilizantes = new ArrayList<>();
        String body = """
                { "npkToCover": { "n": 100, "p": 20, "k": 30 } }
                """;
        JsonNode fertJson = sativumService.post("/nutrients/fertilizers/recommendation", body);
        if (fertJson != null && fertJson.isArray()) fertJson.forEach(fertilizantes::add);

        // --- Modelo ---
        model.addAttribute("semanas",          semanas);
        model.addAttribute("tareas",           tareas);
        model.addAttribute("mes",              mesActual);
        model.addAttribute("anio",             anioActual);
        model.addAttribute("nombreMes",        nombreMes);
        model.addAttribute("zona",             zona);
        model.addAttribute("siembrasMes",      siembrasMes);
        model.addAttribute("cultivosClima",    siembrasAjustadas);
        model.addAttribute("clima",            clima);
        model.addAttribute("plagas",           plagas.isEmpty() ? null : plagas);
        model.addAttribute("fertilizantes",    fertilizantes.isEmpty() ? null : fertilizantes);
        model.addAttribute("hoy",              hoy.getDayOfMonth());

        return "calendario";
    }
}

