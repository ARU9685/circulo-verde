package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegistroController {

    private final UsuarioRepository repo;

    public RegistroController(UsuarioRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormulario(@Valid @ModelAttribute Usuario usuario,
                                     BindingResult result) {

        if (repo.findByNombre(usuario.getNombre()) != null) {
            result.rejectValue("nombre", null, "Este nombre ya está registrado");
            return "registro";
        }

        if (result.hasErrors()) {
            return "registro";
        }

        repo.save(usuario);

        return "redirect:/huerto-login";
    }
}

