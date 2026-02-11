package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import com.circuloverde.circulo_verde.service.ZonaClimaticaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RegistroController {

    private final UsuarioRepository repo;
    private final ZonaClimaticaService zonaClimaticaService;

    public RegistroController(UsuarioRepository repo, ZonaClimaticaService zonaClimaticaService) {

        this.repo = repo;
        this.zonaClimaticaService = zonaClimaticaService;
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
        // asignar zona segun ciudad
        String zona = zonaClimaticaService.obtenerZonaPorCiudad(usuario.getCiudad());
        usuario.setZonaClimatica(zona);

        repo.save(usuario);

        return "redirect:/huerto-login";
    }
}

