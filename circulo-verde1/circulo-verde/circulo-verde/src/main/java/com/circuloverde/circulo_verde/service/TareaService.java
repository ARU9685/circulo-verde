package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;


    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public void guardar(Tarea tarea) {
        tareaRepository.save(tarea);
    }

    public Tarea buscarPorId(Long id) {
        return tareaRepository.findById(id).orElse(null);
    }
    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    public List<Tarea> obtenerTareasDelMes(int año, int mes) {
        String añoMes = String.format("%04d-%02d", año, mes);
        return tareaRepository.findByFechaStartingWith(añoMes);
    }

    public Tarea obtenerProximaTarea(Long usuarioId) {
        return tareaRepository.findFirstByIdUsuarioOrderByFechaAsc(usuarioId);
    }
}

