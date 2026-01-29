package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByNombre(String nombre);
}


