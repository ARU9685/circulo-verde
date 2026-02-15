package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByFechaStartingWith(String añoMes);
}

