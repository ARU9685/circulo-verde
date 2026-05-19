package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.UsuarioService;
import com.circuloverde.circulo_verde.service.ZonaClimaticaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CompletarPerfilController {

    private final UsuarioService usuarioService;
    private final ZonaClimaticaService zonaClimaticaService;

    public CompletarPerfilController(UsuarioService usuarioService,
                                     ZonaClimaticaService zonaClimaticaService) {
        this.usuarioService = usuarioService;
        this.zonaClimaticaService = zonaClimaticaService;
    }

    @GetMapping("/completar-perfil")
    public String mostrarFormulario(HttpSession session, Model model) {
        String email = (String) session.getAttribute("googleEmail");
        if (email == null) return "redirect:/login";

        String nombreGoogle = (String) session.getAttribute("googleNombre");
        model.addAttribute("email", email);
        model.addAttribute("nombreSugerido", nombreGoogle != null
                ? nombreGoogle.replaceAll("\\s+", "") : "");
        return "completar-perfil";
    }

    @PostMapping("/completar-perfil")
    public String guardarPerfil(@RequestParam String nombre,
                                @RequestParam String ciudad,
                                HttpSession session) {

        String email = (String) session.getAttribute("googleEmail");
        if (email == null) return "redirect:/login";

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContrasenia("GOOGLE_" + System.currentTimeMillis());
        usuario.setCiudad(ciudad);
        usuario.setZonaClimatica(zonaClimaticaService.obtenerZonaPorCiudad(ciudad));

        Usuario guardado = usuarioService.registrar(usuario);

        session.removeAttribute("googleEmail");
        session.removeAttribute("googleNombre");
        session.setAttribute("usuarioHuerto", guardado);

        return "redirect:/panel";
    }
}