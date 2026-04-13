package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inicio")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> listar() {
        return service.listarUsuarios();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return service.guardarUsuario(usuario);
    }
}
