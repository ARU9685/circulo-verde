package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.EntradaDiario;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.DiarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class DiarioController {

    private final DiarioRepository diarioRepository;

    public DiarioController(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }

    @GetMapping("/diario")
    public String mostrarDiario(HttpSession session,Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        // Entradas simuladas del diario
        List<EntradaDiario> entradas =
                diarioRepository.findByIdUsuarioOrderByFechaDesc(usuario.getId());

        model.addAttribute("entradas", entradas);

        return "diario";
    }

    // MOSTRAR FORMULARIO PARA NUEVA ENTRADA
    @GetMapping("/diario/nueva")
    public String nuevaEntradaForm(Model model) {
        model.addAttribute("entrada", new EntradaDiario());
        return "diario-form"; // carga diario-form.html
    }

    // GUARDAR NUEVA ENTRADA
    @PostMapping("/diario/guardar")
        public String guardarEntrada(@ModelAttribute EntradaDiario entrada,
                                     HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        // Asignar la entrada al usuario registrado
        entrada.setIdUsuario(usuario.getId());

        // Guardar en BD
        diarioRepository.save(entrada);

        return "redirect:/diario";
    }

}

