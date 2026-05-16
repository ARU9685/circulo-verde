package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deberiaBuscarUsuarioPorEmail() {

        Usuario usuario = new Usuario();

        usuario.setEmail("test@test.com");
        usuario.setNombre("Test");

        when(usuarioRepository.findByEmail("test@test.com"))
                .thenReturn(usuario);

        Usuario resultado =
                usuarioService.buscarPorEmail("test@test.com");

        assertNotNull(resultado);

        assertEquals(
                "test@test.com",
                resultado.getEmail()
        );

        verify(usuarioRepository, times(1))
                .findByEmail("test@test.com");
    }

    @Test
    void deberiaLanzarErrorSiUsuarioNoExiste() {

        when(usuarioRepository.findByEmail("fake@test.com"))
                .thenReturn(null);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> usuarioService.buscarPorEmail(
                                "fake@test.com"
                        )
                );

        assertEquals(
                "Usuario no encontrado",
                exception.getMessage()
        );
    }
}