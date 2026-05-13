package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    private final UsuarioService usuarioService;

    public InicioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Authentication auth, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null && auth != null) {
            usuario = usuarioService.buscarPorNombre(auth.getName());
            session.setAttribute("usuarioHuerto", usuario);
        }
        if (usuario != null) {
            model.addAttribute("nombre", usuario.getNombre());
        }
        return "inicio";
    }
}

