package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.Usuario;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deberiaBuscarUsuarioPorEmail() {

        Usuario usuario = new Usuario();

        usuario.setNombre("Test");
        usuario.setEmail("test@test.com");

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado =
                usuarioRepository.findByEmail("test@test.com");

        assertTrue(resultado.isPresent());

        assertEquals(
                "test@test.com",
                resultado.get().getEmail()
        );
    }
}
