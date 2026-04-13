package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HuertoLoginController {

    private final UsuarioService usuarioService;

    public HuertoLoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Tras el login de Spring Security, carga el Usuario de BD en sesión
     * para que los controllers puedan acceder a sus datos (ciudad, zona...).
     */
    @GetMapping("/post-login")
    public String postLogin(Authentication auth, HttpSession session) {
        if (auth != null && auth.isAuthenticated()) {
            Usuario usuario = usuarioService.buscarPorNombre(auth.getName());
            session.setAttribute("usuarioHuerto", usuario);
        }
        return "redirect:/panel";
    }
}

