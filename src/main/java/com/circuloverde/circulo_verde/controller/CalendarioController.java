package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.InfoCultivo;
import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final AemetService aemetService;

    public CalendarioController(CalendarioSiembraService calendarioSiembraService,
                                TareaService tareaService,
                                SativumService sativumService,
                                WeatherService weatherService,
                                AemetService aemetService) {
        this.calendarioSiembraService = calendarioSiembraService;
        this.tareaService  = tareaService;
        this.sativumService = sativumService;
        this.weatherService = weatherService;
        this.aemetService   = aemetService;
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
        nombreMes = nombreMes.substring(0,1).toUpperCase() + nombreMes.substring(1);

        // Semanas del mes
        int diasMes = ym.lengthOfMonth();
        int diaSemanaInicio = ym.atDay(1).getDayOfWeek().getValue();
        List<List<Integer>> semanas = new ArrayList<>();
        List<Integer> semAct = new ArrayList<>();
        for (int i = 1; i < diaSemanaInicio; i++) semAct.add(null);
        for (int dia = 1; dia <= diasMes; dia++) {
            semAct.add(dia);
            if (semAct.size() == 7) { semanas.add(semAct); semAct = new ArrayList<>(); }
        }
        if (!semAct.isEmpty()) {
            while (semAct.size() < 7) semAct.add(null);
            semanas.add(semAct);
        }

        // Tareas del mes
        List<Tarea> tareas = tareaService.obtenerTareasDelMes(usuario.getId(), anioActual, mesActual);

        // Cultivos por zona
        String zona = usuario.getZonaClimatica();
        List<InfoCultivo> cultivosDelMes = calendarioSiembraService.obtenerCultivosDelMes(zona, mesActual);
        final int mf = mesActual;

        List<InfoCultivo> enSemillero    = cultivosDelMes.stream().filter(c -> c.getMesesSemillero()     != null && c.getMesesSemillero().contains(mf)).collect(Collectors.toList());
        List<InfoCultivo> siembraDirecta = cultivosDelMes.stream().filter(c -> c.getMesesSiembraDirecta()!= null && c.getMesesSiembraDirecta().contains(mf)).collect(Collectors.toList());
        List<InfoCultivo> enPlantacion   = cultivosDelMes.stream().filter(c -> c.getMesesPlantacion()    != null && c.getMesesPlantacion().contains(mf)).collect(Collectors.toList());
        List<InfoCultivo> enCosecha      = cultivosDelMes.stream().filter(c -> c.getMesesCosecha()       != null && c.getMesesCosecha().contains(mf)).collect(Collectors.toList());

        Map<String, String> accionesPorCultivo = new LinkedHashMap<>();
        for (InfoCultivo c : cultivosDelMes)
            accionesPorCultivo.put(c.getNombre(), calendarioSiembraService.obtenerAccionDelMes(c, mesActual));

        // Clima OWM
        Map<String, String> clima = weatherService.obtenerTiempo(usuario.getCiudad());
        List<String> siembrasAjustadas = calendarioSiembraService.ajustarSiembrasPorClima(
                cultivosDelMes.stream().map(InfoCultivo::getNombre).collect(Collectors.toList()), clima);

        // AEMET
        List<Map<String, String>> pronosticoAemet = aemetService.obtenerPronostico7dias(usuario.getCiudad());
        List<Map<String, String>> alertasAemet    = aemetService.generarAlertas(usuario.getCiudad());

        // Sativum
        List<JsonNode> plagas = new ArrayList<>();
        JsonNode pj = sativumService.get("/pests?crop=1");
        if (pj != null && pj.isArray()) pj.forEach(plagas::add);

        List<JsonNode> fertilizantes = new ArrayList<>();
        JsonNode fj = sativumService.post("/nutrients/fertilizers/recommendation", "{ \"npkToCover\": { \"n\": 100, \"p\": 20, \"k\": 30 } }");
        if (fj != null && fj.isArray()) fj.forEach(fertilizantes::add);

        model.addAttribute("semanas",            semanas);
        model.addAttribute("tareas",             tareas);
        model.addAttribute("mes",                mesActual);
        model.addAttribute("anio",               anioActual);
        model.addAttribute("nombreMes",          nombreMes);
        model.addAttribute("zona",               zona);
        model.addAttribute("cultivosDelMes",     cultivosDelMes);
        model.addAttribute("enSemillero",        enSemillero);
        model.addAttribute("siembraDirecta",     siembraDirecta);
        model.addAttribute("enPlantacion",       enPlantacion);
        model.addAttribute("enCosecha",          enCosecha);
        model.addAttribute("accionesPorCultivo", accionesPorCultivo);
        model.addAttribute("siembrasAjustadas",  siembrasAjustadas);
        model.addAttribute("clima",              clima);
        model.addAttribute("pronosticoAemet",    pronosticoAemet);
        model.addAttribute("alertasAemet",       alertasAemet);
        model.addAttribute("plagas",             plagas.isEmpty() ? null : plagas);
        model.addAttribute("fertilizantes",      fertilizantes.isEmpty() ? null : fertilizantes);
        model.addAttribute("hoy",                hoy.getDayOfMonth());
        model.addAttribute("mesHoy",             hoy.getMonthValue());
        model.addAttribute("anioHoy",            hoy.getYear());

        return "calendario";
    }
}