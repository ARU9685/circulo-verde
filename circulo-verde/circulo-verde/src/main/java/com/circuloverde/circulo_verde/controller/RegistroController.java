package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import com.circuloverde.circulo_verde.service.UsuarioService;
import com.circuloverde.circulo_verde.service.ZonaClimaticaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ZonaClimaticaService zonaClimaticaService;

    public RegistroController(UsuarioRepository usuarioRepository,
                              UsuarioService usuarioService,
                              ZonaClimaticaService zonaClimaticaService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.zonaClimaticaService = zonaClimaticaService;
    }

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        System.out.println(">>> GET /registro alcanzado");
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormulario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                     BindingResult result,
                                     Model model) {

        System.out.println(">>> POST /registro alcanzado: " + usuario.getNombre());

        // nombre duplicado
        if (usuarioRepository.findByNombre(usuario.getNombre()) != null) {
            result.rejectValue("nombre", "duplicado", "Este nombre ya está registrado");
        }

        // email duplicado
        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
                result.rejectValue("email", "duplicado", "Este email ya está registrado");
            }
        } else {
            usuario.setEmail(null);
        }

        // Verificación anti-bot: confirmar contraseña
        String confirmar = model.asMap().containsKey("confirmarContrasenia")
                ? (String) model.asMap().get("confirmarContrasenia") : null;

        if (result.hasErrors()) {
            System.out.println(">>> Errores de validación: " + result.getAllErrors());
            return "registro";
        }

        try {
            String zona = zonaClimaticaService.obtenerZonaPorCiudad(usuario.getCiudad());
            usuario.setZonaClimatica(zona);
            usuarioService.registrar(usuario);
            System.out.println(">>> Usuario registrado OK: " + usuario.getNombre());
            return "redirect:/login?registroOk";
        } catch (Exception e) {
            System.out.println(">>> Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorGeneral", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }
}
