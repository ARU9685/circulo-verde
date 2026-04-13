package com.circuloverde.circulo_verde.controller;

import com.circuloverde.circulo_verde.service.CalendarioSiembraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SiembrasApiController {

    private final CalendarioSiembraService calendarioSiembraService;

    public SiembrasApiController(CalendarioSiembraService calendarioSiembraService) {
        this.calendarioSiembraService = calendarioSiembraService;
    }

    @GetMapping("/api/siembras")
    public List<String> obtenerSiembras(
            @RequestParam String zona,
            @RequestParam int mes
    ) {
        return calendarioSiembraService.obtenerSiembrasDelMes(zona, mes);
    }
}

