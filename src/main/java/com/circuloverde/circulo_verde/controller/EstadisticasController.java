package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.DiarioService;
import com.circuloverde.circulo_verde.service.InventarioService;
import com.circuloverde.circulo_verde.service.TareaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class EstadisticasController {

    private final TareaService tareaService;
    private final InventarioService inventarioService;
    private final DiarioService diarioService;

    public EstadisticasController(TareaService tareaService,
                                  InventarioService inventarioService,
                                  DiarioService diarioService) {
        this.tareaService = tareaService;
        this.inventarioService = inventarioService;
        this.diarioService = diarioService;
    }

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Authentication auth, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/login";

        Long uid = usuario.getId();

        // Contadores reales
        model.addAttribute("productosInventario", inventarioService.contarProductos(uid));
        model.addAttribute("entradasDiario",      diarioService.contarEntradas(uid));

        // Tareas de los últimos 6 meses para el gráfico de líneas
        LocalDate hoy = LocalDate.now();
        List<String> meses   = new java.util.ArrayList<>();
        List<Integer> totales = new java.util.ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            LocalDate fecha = hoy.minusMonths(i);
            int m = fecha.getMonthValue();
            int a = fecha.getYear();
            String nombreMes = fecha.getMonth()
                    .getDisplayName(java.time.format.TextStyle.SHORT, new java.util.Locale("es"));
            meses.add(nombreMes.substring(0,1).toUpperCase() + nombreMes.substring(1));
            totales.add(tareaService.obtenerTareasDelMes(uid, a, m).size());
        }
        model.addAttribute("mesesLabels",  meses);
        model.addAttribute("mesesTotales", totales);

        // Total tareas registradas (todos los meses)
        int totalTareas = totales.stream().mapToInt(Integer::intValue).sum();
        model.addAttribute("tareasCompletadas", totalTareas);

        // Distribución inventario por categoría
        List<com.circuloverde.circulo_verde.model.ProductoInventario> productos =
                inventarioService.obtenerProductos(uid);

        java.util.Map<String, Long> porCategoria = productos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        p -> p.getCategoria() != null ? p.getCategoria() : "otro",
                        java.util.stream.Collectors.counting()
                ));

        model.addAttribute("categoriasLabels",  new java.util.ArrayList<>(porCategoria.keySet()));
        model.addAttribute("categoriasTotales", new java.util.ArrayList<>(porCategoria.values()));

        return "estadisticas";
    }
}


