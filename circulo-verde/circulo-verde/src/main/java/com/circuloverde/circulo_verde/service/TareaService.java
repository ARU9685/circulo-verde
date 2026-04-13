package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.Tarea;
import com.circuloverde.circulo_verde.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
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

    public List<Tarea> obtenerTareasDelMes(Long usuarioId, int año, int mes) {
        YearMonth ym = YearMonth.of(año, mes);
        LocalDate inicio = ym.atDay(1);
        LocalDate fin    = ym.atEndOfMonth();
        return tareaRepository.findByIdUsuarioAndFechaBetweenOrderByFechaAsc(usuarioId, inicio, fin);
    }

    /** Devuelve la próxima tarea futura (hoy inclusive) del usuario */
    public Tarea obtenerProximaTarea(Long usuarioId) {
        return tareaRepository.findFirstByIdUsuarioAndFechaGreaterThanEqualOrderByFechaAsc(
                usuarioId, LocalDate.now());
    }

    public boolean perteneceAlUsuario(Long tareaId, Long usuarioId) {
        Tarea t = tareaRepository.findById(tareaId).orElse(null);
        return t != null && t.getIdUsuario().equals(usuarioId);
    }
}

