package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class PanelController {

    private final UsuarioService usuarioService;
    private final WeatherService weatherService;
    private final RecomendacionesService recomendacionesService;
    private final CalendarioSiembraService calendarioSiembraService;
    private final DiarioService diarioService;
    private final TareaService tareaService;
    private final InventarioService inventarioService;

    public PanelController(UsuarioService usuarioService,
                           WeatherService weatherService,
                           RecomendacionesService recomendacionesService,
                           CalendarioSiembraService calendarioSiembraService,
                           DiarioService diarioService,
                           TareaService tareaService,
                           InventarioService inventarioService) {
        this.usuarioService = usuarioService;
        this.weatherService = weatherService;
        this.recomendacionesService = recomendacionesService;
        this.calendarioSiembraService = calendarioSiembraService;
        this.diarioService = diarioService;
        this.tareaService = tareaService;
        this.inventarioService = inventarioService;
    }

    @GetMapping("/panel")
    public String mostrarPanel(Authentication auth, HttpSession session, Model model) {

        // Asegurar que el usuario está en sesión (por si accede directamente sin pasar por /post-login)
        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null && auth != null) {
            usuario = usuarioService.buscarPorNombre(auth.getName());
            session.setAttribute("usuarioHuerto", usuario);
        }
        if (usuario == null) return "redirect:/login";

        String ciudad = usuario.getCiudad();
        String zona   = usuario.getZonaClimatica();

        // Clima
        Map<String, String> tiempo = weatherService.obtenerTiempo(ciudad);
        model.addAttribute("tiempoTexto", tiempo.get("texto"));
        model.addAttribute("tiempoIcono", tiempo.get("icono"));
        model.addAttribute("climaAgricola", Map.of(
                "current", Map.of(
                        "temp",       tiempo.get("temperatura"),
                        "humidity",   tiempo.get("humedad"),
                        "wind_speed", tiempo.get("viento"),
                        "uvi",        "N/D"
                )
        ));

        // Datos del usuario
        model.addAttribute("nombre", usuario.getNombre());
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("zona",   zona);

        // Pronóstico (para el panel de 5 días)
        double[] coords = weatherService.obtenerCoordenadas(ciudad);
        JsonNode pronostico = weatherService.obtenerPronostico(coords[0], coords[1]);
        model.addAttribute("pronostico", pronostico);

        // Próxima tarea
        Tarea proxima = tareaService.obtenerProximaTarea(usuario.getId());
        model.addAttribute("tareas", proxima != null ? List.of(proxima) : List.of());

        // Contadores
        model.addAttribute("productos",     inventarioService.contarProductos(usuario.getId()));
        model.addAttribute("entradasDiario", diarioService.contarEntradas(usuario.getId()));

        // Consejos y cultivos por zona
        model.addAttribute("cultivos",    recomendacionesService.obtenerCultivos(zona));
        model.addAttribute("consejoZona", recomendacionesService.obtenerConsejo(zona));
        model.addAttribute("consejo",
                "Hoy en " + ciudad + " (" + zona + "): " + recomendacionesService.obtenerConsejo(zona));

        // Siembras del mes actual
        model.addAttribute("siembrasMes", calendarioSiembraService.obtenerSiembrasDelMes(zona));

        return "panel";
    }
}
