package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.TareaService;
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

    @GetMapping("/tarea-nueva")
    public String nuevaTarea(Model model, HttpSession session) {
        if (usuarioEnSesion(session) == null) return "redirect:/login";
        model.addAttribute("tarea", new Tarea());
        return "tarea-form";
    }

    @PostMapping("/tarea-guardar")
    public String guardarTarea(@ModelAttribute Tarea tarea, HttpSession session) {
        Usuario usuario = usuarioEnSesion(session);
        if (usuario == null) return "redirect:/login";
        tarea.setIdUsuario(usuario.getId());
        tareaService.guardar(tarea);
        return "redirect:/calendario";
    }

    @GetMapping("/tarea-editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = usuarioEnSesion(session);
        if (usuario == null) return "redirect:/login";

        if (!tareaService.perteneceAlUsuario(id, usuario.getId())) {
            return "redirect:/calendario";
        }
        model.addAttribute("tarea", tareaService.buscarPorId(id));
        return "tarea-form";
    }

    @GetMapping("/tarea-eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, HttpSession session) {
        Usuario usuario = usuarioEnSesion(session);
        if (usuario == null) return "redirect:/login";

        if (tareaService.perteneceAlUsuario(id, usuario.getId())) {
            tareaService.eliminar(id);
        }
        return "redirect:/calendario";
    }

    private Usuario usuarioEnSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioHuerto");
    }
}

