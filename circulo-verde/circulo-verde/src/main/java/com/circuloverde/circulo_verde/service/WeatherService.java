package com.circuloverde.circulo_verde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;


@Service
public class WeatherService {

    private final String API_KEY = "6a9235f1d723a811a3f69e3d791e71d7";

    public Map<String, String> obtenerTiempo(String ciudad) {
        Map<String, String> datos = new HashMap<>();

        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + ciudad + ",es&APPID="
                    + API_KEY + "&lang=es";

            RestTemplate restTemplate = new RestTemplate();
            String respuesta = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(respuesta);

            //descripción
            String descripcion = json.get("weather").get(0).get("description").asText();

            // Temperatura en Kelvin → Celsius
            double tempKelvin = json.get("main").get("temp").asDouble();
            double tempCelsius = tempKelvin - 273.15;
            String temperatura = String.format("%.1f", tempCelsius).replace(",", ".");

            // Humedad
            String humedad = json.get("main").get("humidity").asText().replace(",", ".");

            // Viento
            String viento = json.get("wind").get("speed").asText().replace(",", ".");

            String icono = json.get("weather").get(0).get("icon").asText();

            datos.put("texto", "Hoy hace " + temperatura + "°C en " + ciudad + " y está " + descripcion);
            datos.put("icono", "https://openweathermap.org/img/wn/" + icono + "@2x.png");
            datos.put("temperatura", temperatura);
            datos.put("humedad", humedad);
            datos.put("viento", viento);
            datos.put("descripcion", descripcion);

            return datos;

        } catch (Exception e) {
            datos.put("texto", "No se pudo obtener el tiempo");
            datos.put("icono", "");
            datos.put("temperatura", "N/D");
            datos.put("humedad", "N/D");
            datos.put("viento", "N/D");
            return datos;
        }
    }

    public JsonNode obtenerPronostico(double lat, double lon) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat +
                    "&lon=" + lon +
                    "&units=metric&lang=es&appid=" + API_KEY;

            RestTemplate restTemplate = new RestTemplate();
            String respuesta = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //Metodo para SATIVUM
    public double[] obtenerCoordenadas(String ciudad) {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + ciudad + ",es&APPID="
                    + API_KEY + "&lang=es";

            RestTemplate restTemplate = new RestTemplate();
            String respuesta = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(respuesta);

            double lat = json.get("coord").get("lat").asDouble();
            double lon = json.get("coord").get("lon").asDouble();

            return new double[]{lat, lon};

        } catch (Exception e) {
            return new double[]{0, 0};
        }
    }
}


