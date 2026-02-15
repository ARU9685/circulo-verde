package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HuertoLoginController {

    private final UsuarioRepository usuarioRepository;

    public HuertoLoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/huerto-login")
    public String mostrarLogin() {
        return "huerto-login";
    }

    @PostMapping("/huerto-login")
    public String procesarLogin(@RequestParam String nombre,
                                @RequestParam String contrasenia,
                                HttpSession session,
                                Model model) {

        Usuario usuario = usuarioRepository.findByNombre(nombre);

        if (usuario == null || usuario.getContrasenia() == null ||
                !usuario.getContrasenia().equals(contrasenia)) {
            model.addAttribute("error", "Nombre o contraseña incorrectos");
            return "/huerto-login";
        }

        session.setAttribute("usuarioHuerto", usuario);

        return "redirect:/panel";
    }
}

