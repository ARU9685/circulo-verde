package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.model.ProductoInventario;
import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.service.InventarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/inventario")
    public String mostrarInventario(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/huerto-login";

        List<ProductoInventario> productos =
                inventarioService.obtenerProductos(usuario.getId());

        model.addAttribute("productos", productos);

        return "inventario";
    }

    @GetMapping("/inventario/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new ProductoInventario());
        return "inventario-form";
    }

    @PostMapping("/inventario/guardar")
    public String guardarProducto(@ModelAttribute ProductoInventario producto,
                                  HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/huerto-login";

        producto.setIdUsuario(usuario.getId());
        inventarioService.guardarProducto(producto);

        return "redirect:/inventario";
    }

    @GetMapping("/inventario/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioHuerto");
        if (usuario == null) return "redirect:/huerto-login";

        ProductoInventario producto = inventarioService.buscarPorId(id);
        model.addAttribute("producto", producto);

        return "inventario-form";
    }

    @GetMapping("/inventario/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return "redirect:/inventario";
    }
}


