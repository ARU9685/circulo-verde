package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByIdUsuarioOrderByFechaAsc(Long idUsuario);

    List<Tarea> findByIdUsuarioAndFechaBetweenOrderByFechaAsc(Long idUsuario, LocalDate inicio, LocalDate fin);

    // Próxima tarea: desde hoy en adelante
    Tarea findFirstByIdUsuarioAndFechaGreaterThanEqualOrderByFechaAsc(Long idUsuario, LocalDate hoy);

}

