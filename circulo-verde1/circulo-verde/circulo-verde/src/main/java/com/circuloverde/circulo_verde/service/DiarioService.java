package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.repository.DiarioRepository;
import org.springframework.stereotype.Service;

@Service
public class DiarioService {

    private final DiarioRepository diarioRepository;

    public DiarioService(DiarioRepository diarioRepository) {
        this.diarioRepository = diarioRepository;
    }

    public int contarEntradas(Long usuarioId) {
        return diarioRepository.findByIdUsuarioOrderByFechaDesc(usuarioId).size();
    }
}

