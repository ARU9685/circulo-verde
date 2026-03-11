package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.service.WeatherService;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import com.circuloverde.circulo_verde.service.RecomendacionesService;
import com.circuloverde.circulo_verde.service.CalendarioSiembraService;
import com.circuloverde.circulo_verde.service.DiarioService;
import com.circuloverde.circulo_verde.service.SativumService;
import  com.circuloverde.circulo_verde.service.TareaService;
import  com.circuloverde.circulo_verde.service.InventarioService;
import com.circuloverde.circulo_verde.model.Tarea;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;


import java.util.List;

@Controller
public class PanelController {

    private final UsuarioRepository usuarioRepository;
    private final WeatherService weatherService;
    private final RecomendacionesService recomendacionesService;
    private final CalendarioSiembraService calendarioSiembraService;
    private final SativumService sativumService;
    private final DiarioService diarioService;
    private final TareaService tareaService;
    private final InventarioService inventarioService;

    public PanelController(UsuarioRepository usuarioRepository,
                           WeatherService weatherService,
                           RecomendacionesService recomendacionesService,
                           CalendarioSiembraService calendarioSiembraService,
                           SativumService sativumService, DiarioService diarioService, TareaService tareaService,
                           InventarioService inventarioService) {
        this.usuarioRepository = usuarioRepository;
        this.weatherService = weatherService;
        this.recomendacionesService = recomendacionesService;
        this.calendarioSiembraService = calendarioSiembraService;
        this.sativumService = sativumService;
        this.diarioService = diarioService;
        this.tareaService = tareaService;
        this.inventarioService = inventarioService;
    }

    @GetMapping("/panel")
    public String mostrarPanel(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        String ciudad = usuario.getCiudad();
        String zona = usuario.getZonaClimatica();

        // CLIMA
        Map<String, String> tiempo = weatherService.obtenerTiempo(ciudad);
        model.addAttribute("tiempoTexto", tiempo.get("texto"));
        model.addAttribute("tiempoIcono", tiempo.get("icono"));

        // Simulación de clima agrícola usando datos disponibles
        model.addAttribute("climaAgricola", Map.of(
                "current", Map.of(
                        "temp", tiempo.get("temperatura"),
                        "humidity", tiempo.get("humedad"),
                        "wind_speed", tiempo.get("viento"),
                        "uvi", "N/D" // No disponible sin OneCall
                )
        ));

        // datos del USUARIO
        model.addAttribute("nombre", usuario.getNombre());
        model.addAttribute("ciudad", usuario.getCiudad());
        model.addAttribute("zona", usuario.getZonaClimatica());


        // 2. COORDENADAS PARA SATIVUM //

        double[] coords = weatherService.obtenerCoordenadas(ciudad);
        double lat = coords[0];
        double lon = coords[1];

        JsonNode pronostico = weatherService.obtenerPronostico(lat, lon);
        model.addAttribute("pronostico", pronostico);

        Tarea proxima = tareaService.obtenerProximaTarea(usuario.getId());
        model.addAttribute("tareas", proxima != null ? List.of(proxima) : List.of());

        int productos = inventarioService.contarProductos(usuario.getId());
        model.addAttribute("productos", productos);

        int entradasDiario = diarioService.contarEntradas(usuario.getId());
        model.addAttribute("entradasDiario", entradasDiario);

        model.addAttribute("consejo",
                "Hoy en " + usuario.getCiudad() + " (" + usuario.getZonaClimatica() + "):" + recomendacionesService.obtenerConsejo(zona));

        //consejo según zona
        List<String> cultivos = recomendacionesService.obtenerCultivos(zona);
        String consejoZona = recomendacionesService.obtenerConsejo(zona);

        model.addAttribute("cultivos", cultivos);
        model.addAttribute("consejoZona", consejoZona);

        List<String> siembrasMes = calendarioSiembraService.obtenerSiembrasDelMes(zona);
        model.addAttribute("siembrasMes", siembrasMes);


        return "/panel";
    }
}

