package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.InfoCultivo;
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
import java.util.stream.Collectors;

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
            @RequestParam(required = false) Integer anio,
            Authentication auth,
            HttpSession session,
            Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/login";

        LocalDate hoy = LocalDate.now();
        int mesActual  = (mes  != null) ? mes  : hoy.getMonthValue();
        int anioActual = (anio != null) ? anio : hoy.getYear();

        if (mesActual < 1)  { mesActual = 12; anioActual--; }
        if (mesActual > 12) { mesActual = 1;  anioActual++; }

        YearMonth ym = YearMonth.of(anioActual, mesActual);
        String nombreMes = ym.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
        nombreMes = nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1);

        // Semanas del mes
        int diasMes = ym.lengthOfMonth();
        int diaSemanaInicio = ym.atDay(1).getDayOfWeek().getValue();
        List<List<Integer>> semanas = new ArrayList<>();
        List<Integer> semanaActual = new ArrayList<>();
        for (int i = 1; i < diaSemanaInicio; i++) semanaActual.add(null);
        for (int dia = 1; dia <= diasMes; dia++) {
            semanaActual.add(dia);
            if (semanaActual.size() == 7) { semanas.add(semanaActual); semanaActual = new ArrayList<>(); }
        }
        if (!semanaActual.isEmpty()) {
            while (semanaActual.size() < 7) semanaActual.add(null);
            semanas.add(semanaActual);
        }

        // Tareas del usuario en este mes
        List<Tarea> tareas = tareaService.obtenerTareasDelMes(usuario.getId(), anioActual, mesActual);

        // Cultivos activos este mes con toda su información
        String zona = usuario.getZonaClimatica();
        List<InfoCultivo> cultivosDelMes = calendarioSiembraService.obtenerCultivosDelMes(zona, mesActual);

        // Separar por acción para mostrar secciones en el template
        final int mesFinal = mesActual;
        List<InfoCultivo> enSemillero = cultivosDelMes.stream()
                .filter(c -> c.getMesesSemillero() != null && c.getMesesSemillero().contains(mesFinal))
                .collect(Collectors.toList());

        List<InfoCultivo> siembraDirecta = cultivosDelMes.stream()
                .filter(c -> c.getMesesSiembraDirecta() != null && c.getMesesSiembraDirecta().contains(mesFinal))
                .collect(Collectors.toList());

        List<InfoCultivo> enPlantacion = cultivosDelMes.stream()
                .filter(c -> c.getMesesPlantacion() != null && c.getMesesPlantacion().contains(mesFinal))
                .collect(Collectors.toList());

        List<InfoCultivo> enCosecha = cultivosDelMes.stream()
                .filter(c -> c.getMesesCosecha() != null && c.getMesesCosecha().contains(mesFinal))
                .collect(Collectors.toList());

        // Clima
        Map<String, String> clima = weatherService.obtenerTiempo(usuario.getCiudad());
        List<String> siembrasAjustadas = calendarioSiembraService.ajustarSiembrasPorClima(
                cultivosDelMes.stream().map(InfoCultivo::getNombre).collect(Collectors.toList()), clima);

        // Sativum
        List<JsonNode> plagas = new ArrayList<>();
        JsonNode plagasJson = sativumService.get("/pests?crop=1");
        if (plagasJson != null && plagasJson.isArray()) plagasJson.forEach(plagas::add);

        List<JsonNode> fertilizantes = new ArrayList<>();
        JsonNode fertJson = sativumService.post("/nutrients/fertilizers/recommendation",
                "{ \"npkToCover\": { \"n\": 100, \"p\": 20, \"k\": 30 } }");
        if (fertJson != null && fertJson.isArray()) fertJson.forEach(fertilizantes::add);

        // Pasar acción del mes a cada cultivo como Map para Thymeleaf
        Map<String, String> accionesPorCultivo = new LinkedHashMap<>();
        for (InfoCultivo c : cultivosDelMes) {
            accionesPorCultivo.put(c.getNombre(),
                    calendarioSiembraService.obtenerAccionDelMes(c, mesActual));
        }

        model.addAttribute("semanas",          semanas);
        model.addAttribute("tareas",           tareas);
        model.addAttribute("mes",              mesActual);
        model.addAttribute("anio",             anioActual);
        model.addAttribute("nombreMes",        nombreMes);
        model.addAttribute("zona",             zona);
        model.addAttribute("cultivosDelMes",   cultivosDelMes);
        model.addAttribute("enSemillero",      enSemillero);
        model.addAttribute("siembraDirecta",   siembraDirecta);
        model.addAttribute("enPlantacion",     enPlantacion);
        model.addAttribute("enCosecha",        enCosecha);
        model.addAttribute("accionesPorCultivo", accionesPorCultivo);
        model.addAttribute("siembrasAjustadas",siembrasAjustadas);
        model.addAttribute("clima",            clima);
        model.addAttribute("plagas",           plagas.isEmpty() ? null : plagas);
        model.addAttribute("fertilizantes",    fertilizantes.isEmpty() ? null : fertilizantes);
        model.addAttribute("hoy",              hoy.getDayOfMonth());
        model.addAttribute("mesHoy",           hoy.getMonthValue());
        model.addAttribute("anioHoy",          hoy.getYear());

        return "calendario";
    }
}