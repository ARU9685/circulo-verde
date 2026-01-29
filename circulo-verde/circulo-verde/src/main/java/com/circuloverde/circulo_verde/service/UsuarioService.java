package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {{}
    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listarUsuarios() {
        return repo.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return repo.save(usuario);

    }
}
