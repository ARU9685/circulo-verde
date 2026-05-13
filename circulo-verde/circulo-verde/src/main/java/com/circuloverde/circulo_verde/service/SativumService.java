package com.circuloverde.circulo_verde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;


@Service
public class SativumService {

    @Value("${sativum.token}")
    private String token;

    private final String BASE = "https://gateway.api.itacyl.es/sativum";

    public JsonNode get(String endpoint) {
        try {
            System.out.println("URL = " + BASE + endpoint);

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey",token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response =
                    rest.exchange(BASE + endpoint, HttpMethod.GET, entity, String.class);
            System.out.println("URL llamada: " + BASE + endpoint);
            System.out.println("Respuesta Sativum: " + response.getBody());


            return new ObjectMapper().readTree(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode post(String endpoint, String bodyJson) {
        try {

            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", token);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(bodyJson, headers);

            ResponseEntity<String> response =
                    rest.exchange(BASE + endpoint, HttpMethod.POST, entity, String.class);
            System.out.println("URL llamada: " + BASE + endpoint);
            System.out.println("Respuesta Sativum: " + response.getBody());


            return new ObjectMapper().readTree(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @PostConstruct
    public void init() {
        System.out.println("TOKEN CARGADO = [" + token + "]");
    }

}

