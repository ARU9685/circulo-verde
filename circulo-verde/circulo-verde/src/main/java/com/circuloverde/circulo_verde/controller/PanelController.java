package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.service.WeatherService;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
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

    public PanelController(UsuarioRepository usuarioRepository, WeatherService weatherService) {
        this.usuarioRepository = usuarioRepository;
        this.weatherService = weatherService;
    }

    @GetMapping("/panel")
    public String mostrarPanel(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        String zona = usuario.getZonaClimatica();

        Map<String, String> tiempo = weatherService.obtenerTiempo(zona);
        model.addAttribute("tiempoTexto", tiempo.get("texto"));
        model.addAttribute("tiempoIcono", tiempo.get("icono"));


        model.addAttribute("nombre", usuario.getNombre());
        model.addAttribute("zona", usuario.getZonaClimatica());
        model.addAttribute("tareas", List.of("Siembra de tomates 24 Jun (mañana)"));
        model.addAttribute("productos", 4);
        model.addAttribute("entradasDiario", 5);
        model.addAttribute("consejo",
                "Hoy en zona " + usuario.getZonaClimatica() + ": ideal para sembrar lechugas");

        return "/panel";
    }
}

