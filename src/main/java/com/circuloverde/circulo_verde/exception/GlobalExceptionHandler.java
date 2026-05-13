package com.circuloverde.circulo_verde.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error("Error no controlado", exception);
        model.addAttribute("error", "Ha ocurrido un error inesperado. Inténtalo más tarde.");
        return "error";
    }
}