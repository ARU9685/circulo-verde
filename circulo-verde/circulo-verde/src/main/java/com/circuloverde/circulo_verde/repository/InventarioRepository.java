package com.circuloverde.circulo_verde.repository;

import com.circuloverde.circulo_verde.model.ProductoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventarioRepository extends JpaRepository<ProductoInventario, Long> {

    List<ProductoInventario> findByIdUsuario(Long idUsuario);

    int countByIdUsuario(Long idUsuario);
}

