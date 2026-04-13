package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.ProductoInventario;
import com.circuloverde.circulo_verde.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public List<ProductoInventario> obtenerProductos(Long usuarioId) {
        return inventarioRepository.findByIdUsuario(usuarioId);
    }

    public int contarProductos(Long usuarioId) {
        return inventarioRepository.countByIdUsuario(usuarioId);
    }

    public void guardarProducto(ProductoInventario producto) {
        inventarioRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        inventarioRepository.deleteById(id);
    }

    public ProductoInventario buscarPorId(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

}

