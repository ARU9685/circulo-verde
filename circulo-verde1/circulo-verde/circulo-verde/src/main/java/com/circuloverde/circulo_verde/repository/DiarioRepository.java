package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.EntradaDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiarioRepository extends JpaRepository<EntradaDiario, Long> {

    // Recuperar las entradas del diario de un usuario ordenadas por fecha descendente
    List<EntradaDiario> findByIdUsuarioOrderByFechaDesc(Long idUsuario);
}

