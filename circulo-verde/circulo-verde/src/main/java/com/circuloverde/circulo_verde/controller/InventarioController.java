package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.ProductoInventario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InventarioController {

    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {

        // Lista simulada de productos del inventario
        List<ProductoInventario> productos = List.of(
                new ProductoInventario("Semillas de tomate", "Semillas", "20 unidades", "Disponible"),
                new ProductoInventario("Abono orgánico", "Fertilizante", "5 kg", "Bajo"),
                new ProductoInventario("Guantes de jardinería", "Herramientas", "1 par", "Disponible")
        );

        model.addAttribute("productos", productos);

        return "inventario"; // carga inventario.html
    }
}

