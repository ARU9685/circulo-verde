package com.circuloverde.circulo_verde.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AemetService {

    @Value("${aemet.api.key:SIN_CLAVE}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Map<String, String> MUNICIPIOS = Map.ofEntries(
            Map.entry("Valencia",              "46250"),
            Map.entry("Castellón",             "12040"),
            Map.entry("Alicante",              "03014"),
            Map.entry("Murcia",                "30030"),
            Map.entry("Madrid",                "28079"),
            Map.entry("Barcelona",             "08019"),
            Map.entry("Sevilla",               "41091"),
            Map.entry("Málaga",                "29067"),
            Map.entry("Bilbao",                "48020"),
            Map.entry("Zaragoza",              "50297"),
            Map.entry("Palma de Mallorca",     "07040"),
            Map.entry("Las Palmas",            "35016"),
            Map.entry("Santa Cruz de Tenerife","38038"),
            Map.entry("A Coruña",              "15030"),
            Map.entry("Oviedo",                "33044"),
            Map.entry("Santander",             "39075"),
            Map.entry("Pamplona",              "31201"),
            Map.entry("San Sebastián",         "20069"),
            Map.entry("Vitoria-Gasteiz",       "01059"),
            Map.entry("Vigo",                  "36057"),
            Map.entry("Granada",               "18087"),
            Map.entry("Almería",               "04013"),
            Map.entry("Valladolid",            "47186"),
            Map.entry("Burgos",                "09059"),
            Map.entry("Salamanca",             "37274"),
            Map.entry("Toledo",                "45168"),
            Map.entry("Albacete",              "02003"),
            Map.entry("Cádiz",                 "11012"),
            Map.entry("Huelva",                "21041"),
            Map.entry("Logroño",               "26089"),
            Map.entry("Tarragona",             "43148"),
            Map.entry("Girona",                "17079"),
            Map.entry("León",                  "24089"),
            Map.entry("Lugo",                  "27028"),
            Map.entry("Ourense",               "32054"),
            Map.entry("Pontevedra",            "36038"),
            Map.entry("Córdoba",               "14021"),
            Map.entry("Badajoz",               "06015")
    );

    /** Pronóstico diario AEMET para los próximos 7 días */
    public List<Map<String, String>> obtenerPronostico7dias(String ciudad) {
        List<Map<String, String>> dias = new ArrayList<>();
        if ("SIN_CLAVE".equals(apiKey)) return dias;

        String cod = MUNICIPIOS.get(ciudad);
        if (cod == null) return dias;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();

            // Paso 1: obtener URL de datos
            String url1 = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/"
                    + cod + "/?api_key=" + apiKey;
            ResponseEntity<String> r1 = rt.exchange(url1, HttpMethod.GET, entity, String.class);
            String dataUrl = mapper.readTree(r1.getBody()).get("datos").asText();

            // Paso 2: datos reales
            ResponseEntity<String> r2 = rt.exchange(dataUrl, HttpMethod.GET, entity, String.class);
            JsonNode prediccion = mapper.readTree(r2.getBody()).get(0).get("prediccion").get("dia");

            for (JsonNode dia : prediccion) {
                Map<String, String> d = new LinkedHashMap<>();
                d.put("fecha",       dia.get("fecha").asText().substring(0, 10));
                d.put("tempMax",     safeGet(dia, "temperatura", "maxima"));
                d.put("tempMin",     safeGet(dia, "temperatura", "minima"));
                d.put("probPrec",    safeArr(dia, "probPrecipitacion", 0, "value"));
                d.put("viento",      safeArr(dia, "viento",            0, "velocidad"));
                d.put("estadoCielo", safeArr(dia, "estadoCielo",       0, "descripcion"));
                dias.add(d);
            }
        } catch (Exception e) {
            System.err.println("AEMET error: " + e.getMessage());
        }
        return dias;
    }

    /** Alertas agrícolas basadas en el pronóstico AEMET */
    public List<Map<String, String>> generarAlertas(String ciudad) {
        List<Map<String, String>> alertas = new ArrayList<>();
        List<Map<String, String>> pron = obtenerPronostico7dias(ciudad);

        if (pron.isEmpty()) return alertas;

        for (Map<String, String> d : pron) {
            String fecha  = d.get("fecha");
            int tempMax   = toInt(d.get("tempMax"));
            int tempMin   = toInt(d.get("tempMin"));
            int probPrec  = toInt(d.get("probPrec"));
            int viento    = toInt(d.get("viento"));

            if (tempMin <= 2)
                alertas.add(a("helada",  "🌡️", "Helada el " + fmt(fecha) + " (" + tempMin + "°C). Protege cultivos sensibles.", tempMin <= 0 ? "PELIGRO" : "AVISO"));
            if (tempMax >= 38)
                alertas.add(a("calor",   "🔥", "Calor extremo el " + fmt(fecha) + " (" + tempMax + "°C). Riega al amanecer o al atardecer.", "AVISO"));
            if (probPrec >= 80)
                alertas.add(a("lluvia",  "🌧️", "Lluvia probable el " + fmt(fecha) + " (" + probPrec + "%). Suspende el riego programado.", probPrec >= 95 ? "PELIGRO" : "AVISO"));
            if (viento >= 50)
                alertas.add(a("viento",  "💨", "Viento fuerte el " + fmt(fecha) + " (" + viento + " km/h). Protege tutores y estructuras.", viento >= 70 ? "PELIGRO" : "AVISO"));
            if (probPrec < 20 && tempMax >= 28)
                alertas.add(a("riego",   "💧", fmt(fecha) + ": Sin lluvia y " + tempMax + "°C. Buen día para regar por la mañana temprano.", "INFO"));
        }

        if (alertas.isEmpty())
            alertas.add(a("ok", "✅", "Sin alertas agrícolas para los próximos días. Condiciones favorables.", "INFO"));

        return alertas;
    }

    // helpers
    private Map<String, String> a(String tipo, String icono, String msg, String nivel) {
        return Map.of("tipo", tipo, "icono", icono, "mensaje", msg, "nivel", nivel);
    }
    private String safeGet(JsonNode n, String f, String s) {
        try { return n.get(f).get(s).asText(); } catch (Exception e) { return "0"; }
    }
    private String safeArr(JsonNode n, String f, int i, String s) {
        try { return n.get(f).get(i).get(s).asText(); } catch (Exception e) { return "0"; }
    }
    private int toInt(String v) {
        try { return Integer.parseInt(v == null ? "0" : v.trim()); } catch (Exception e) { return 0; }
    }
    private String fmt(String fecha) {
        if (fecha == null || fecha.length() < 10) return "";
        String[] m = {"","ene","feb","mar","abr","may","jun","jul","ago","sep","oct","nov","dic"};
        return Integer.parseInt(fecha.substring(8,10)) + " " + m[Integer.parseInt(fecha.substring(5,7))];
    }
}

