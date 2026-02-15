package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.service.TareaService;
import com.circuloverde.circulo_verde.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // Mostrar formulario de nueva tarea
    @GetMapping("/tarea-nueva")
    public String nuevaTarea(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) { return "redirect:/huerto-login";
        }
        model.addAttribute("tarea", new Tarea());
        return "tarea-form";
    }

    // Guardar tarea
    @PostMapping("/tarea-guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea,  HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        tareaService.guardar(tarea);
        return "redirect:/calendario";
    }

    // Editar tarea
    @GetMapping("/tarea-editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model,  HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        Tarea tarea = tareaService.buscarPorId(id);
        model.addAttribute("tarea", tarea);
        return "tarea-form";
    }

    // Eliminar tarea
    @GetMapping("/tarea-eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id,  HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) {
            return "redirect:/huerto-login";
        }

        tareaService.eliminar(id);
        return "redirect:/calendario";
    }
}

