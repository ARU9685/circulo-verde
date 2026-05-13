package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.InfoCultivo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarioSiembraService {

    // ─────────────────────────────────────────────
    // API PÚBLICA
    // ─────────────────────────────────────────────

    /** Cultivos cuyo semillero, plantación o cosecha coincide con el mes dado */
    public List<InfoCultivo> obtenerCultivosDelMes(String zona, int mes) {
        return catalogoPorZona(zona).stream()
                .filter(c -> activoEnMes(c, mes))
                .collect(Collectors.toList());
    }

    public List<InfoCultivo> obtenerCultivosDelMes(String zona) {
        return obtenerCultivosDelMes(zona, LocalDate.now().getMonthValue());
    }

    /** Solo nombres — compatibilidad con código anterior */
    public List<String> obtenerSiembrasDelMes(String zona, int mes) {
        return obtenerCultivosDelMes(zona, mes).stream()
                .filter(c -> c.getMesesSemillero() != null && c.getMesesSemillero().contains(mes)
                        || c.getMesesSiembraDirecta() != null && c.getMesesSiembraDirecta().contains(mes)
                        || c.getMesesPlantacion() != null && c.getMesesPlantacion().contains(mes))
                .map(InfoCultivo::getNombre)
                .collect(Collectors.toList());
    }

    public List<String> obtenerSiembrasDelMes(String zona) {
        return obtenerSiembrasDelMes(zona, LocalDate.now().getMonthValue());
    }

    /** Qué se puede hacer con cada cultivo en un mes concreto */
    public String obtenerAccionDelMes(InfoCultivo c, int mes) {
        List<String> acciones = new ArrayList<>();
        if (c.getMesesSemillero() != null && c.getMesesSemillero().contains(mes))
            acciones.add("semillero");
        if (c.getMesesSiembraDirecta() != null && c.getMesesSiembraDirecta().contains(mes))
            acciones.add("siembra directa");
        if (c.getMesesPlantacion() != null && c.getMesesPlantacion().contains(mes))
            acciones.add("plantación");
        if (c.getMesesCosecha() != null && c.getMesesCosecha().contains(mes))
            acciones.add("cosecha");
        return String.join(" · ", acciones);
    }

    public List<String> ajustarSiembrasPorClima(List<String> siembras, Map<String, String> clima) {
        final double temp   = parsearDouble(clima.getOrDefault("temperatura", "20"), 20.0);
        final double viento = parsearDouble(clima.getOrDefault("viento", "0"), 0.0);
        final List<String> calor  = List.of("tomate","pimiento","berenjena","calabacín","pepino","melón","sandía");
        final List<String> frescas= List.of("lechuga","espinaca","acelga");
        return siembras.stream().filter(c -> {
            String n = c.toLowerCase();
            if (temp < 10 && calor.stream().anyMatch(n::contains))   return false;
            if (temp > 32 && frescas.stream().anyMatch(n::contains)) return false;
            return true;
        }).toList();
    }

    // ─────────────────────────────────────────────
    // CATÁLOGOS POR ZONA
    // ─────────────────────────────────────────────

    private List<InfoCultivo> catalogoPorZona(String zona) {
        return switch (zona) {
            case "Mediterránea"  -> catalogoMediterraneo();
            case "Continental"   -> catalogoContinental();
            case "Atlántica"     -> catalogoAtlantico();
            case "Subtropical"   -> catalogoSubtropical();
            default              -> catalogoMediterraneo();
        };
    }

    private boolean activoEnMes(InfoCultivo c, int mes) {
        return contieneM(c.getMesesSemillero(), mes)
                || contieneM(c.getMesesSiembraDirecta(), mes)
                || contieneM(c.getMesesPlantacion(), mes)
                || contieneM(c.getMesesCosecha(), mes);
    }

    private boolean contieneM(List<Integer> lista, int mes) {
        return lista != null && lista.contains(mes);
    }

    // ─────────────────────────────────────────────
    // ZONA MEDITERRÁNEA
    // ─────────────────────────────────────────────
    private List<InfoCultivo> catalogoMediterraneo() {
        return List.of(

                new InfoCultivo("Tomate","fruto","🍅",
                        m(1,2), 50,
                        null,
                        m(3,4,5,6),
                        m(6,7,8,9,10),
                        "Cada 3-4 días en verano, reducir en otoño","goteo",
                        l("Entutorar","Eliminar chupones","Despuntar en septiembre"),
                        "Muy sensible a la sequía en floración"),

                new InfoCultivo("Pimiento","fruto","🫑",
                        m(1,2), 55,
                        null,
                        m(4,5),
                        m(7,8,9,10,11),
                        "Cada 4-5 días, no encharcar","goteo",
                        l("Entutorar","Retirar flores las 2 primeras semanas","Poda en verde"),
                        "No soporta temperaturas por debajo de 10°C"),

                new InfoCultivo("Berenjena","fruto","🍆",
                        m(1,2), 60,
                        null,
                        m(3,4,5),
                        m(6,7,8,9,10,11),
                        "Cada 4-5 días, constante","goteo",
                        l("Entutorar","Poda de rejuvenecimiento","Control de araña roja"),
                        "La más exigente en calor de las solanáceas"),

                new InfoCultivo("Pepino","fruto","🥒",
                        m(2,3,7), 20,
                        null,
                        m(4,5,8),
                        m(6,7,8,9,10),
                        "Cada 3-4 días, muy exigente","goteo",
                        l("Entutorar vertical","Pellizcar guías laterales","Deshojado basal"),
                        "Cosecha frecuente para estimular producción"),

                new InfoCultivo("Calabacín","fruto","🥬",
                        m(3,4,7,8), 15,
                        null,
                        m(4,5,8,9),
                        m(6,7,8,9,10),
                        "Cada 4-5 días, no mojar cuello","goteo o mano",
                        l("Polinización manual si faltan abejas","Retirar hojas viejas","Acolchar suelo"),
                        "Cosechar pequeño para mayor producción"),

                new InfoCultivo("Calabaza","fruto","🎃",
                        m(3,4), 20,
                        null,
                        m(4,5),
                        m(8,9,10),
                        "Cada 6-7 días, reducir al madurar","surco",
                        l("Dejar 2-3 frutos por planta","Acolchar bajo el fruto","Cortar tallo con navaja"),
                        "Cura en seco 10 días antes de guardar"),

                new InfoCultivo("Sandía","fruto","🍉",
                        m(2,3), 30,
                        null,
                        m(4,5),
                        m(7,8,9),
                        "Cada 5-6 días, suprimir al madurar","goteo",
                        l("Aclarar a 1-2 frutos","Acolchar suelo","Girar fruto para coloración uniforme"),
                        "Lista cuando el zarcillo más cercano se seca"),

                new InfoCultivo("Melón","fruto","🍈",
                        m(2,3), 30,
                        null,
                        m(4,5),
                        m(7,8,9),
                        "Cada 6-7 días, muy escaso al madurar","surco",
                        l("Pellizcar guías","Dejar 2-3 frutos","No mojar hojas"),
                        "El aroma y grieta en el pedúnculo indican madurez"),

                new InfoCultivo("Tomate cherry","fruto","🍒",
                        m(1,2), 50,
                        null,
                        m(3,4,5),
                        m(6,7,8,9,10),
                        "Cada 3-4 días","goteo",
                        l("Entutorar","Eliminar chupones","Cosechar por racimos"),
                        "Más resistente a la sequía que el tomate normal"),

                new InfoCultivo("Pimiento italiano","fruto","🌶️",
                        m(1,2), 55,
                        null,
                        m(4,5),
                        m(7,8,9,10),
                        "Cada 4-5 días","goteo",
                        l("Entutorar","Poda ligera"),
                        "Ideal para asar, muy productivo"),

                new InfoCultivo("Judía verde","legumbre","🫘",
                        null, null,
                        m(3,4,5,8,9),
                        m(3,4,5,8,9),
                        m(5,6,7,10,11),
                        "Cada 4-5 días en floración","mano o goteo",
                        l("Entutorar variedades trepadoras","No mojar en floración","Cosechar antes de que madure la vaina"),
                        "Dos ciclos anuales: primavera y otoño"),

                new InfoCultivo("Habas","legumbre","🫘",
                        null, null,
                        m(10,11,12),
                        m(10,11,12),
                        m(3,4,5),
                        "Lluvia natural + riego puntual","mano",
                        l("Despuntar ápice al aparecer pulgón negro","Apuntalar si hay viento"),
                        "Sembrar en otoño para cosechar en primavera"),

                new InfoCultivo("Guisante","legumbre","🟢",
                        null, null,
                        m(10,11,2,3),
                        m(10,11,2,3),
                        m(3,4,5),
                        "Lluvia + riego puntual cada 8 días","mano",
                        l("Entutorar en red","Cosechar joven para terneza"),
                        "No soporta el calor estival"),

                new InfoCultivo("Lechuga","hoja","🥬",
                        m(8,9,1,2), 30,
                        null,
                        m(9,10,11,2,3,4),
                        m(11,12,1,2,3,4,5),
                        "Cada 5-7 días, no mojar cogollo","mano o aspersión",
                        l("Aclareo si siembra directa","Control de babosas","No regar en exceso"),
                        "Varias variedades permiten cosecha casi todo el año"),

                new InfoCultivo("Espinaca","hoja","🌿",
                        m(8,9,2), 0,
                        m(8,9,2,3),
                        m(9,10,2,3),
                        m(10,11,12,1,3,4,5),
                        "Cada 7-8 días","mano",
                        l("Aclareo a 10 cm","Retirar hojas externas","No dejar espolar"),
                        "Prefiere días cortos, se va a semilla con calor"),

                new InfoCultivo("Acelga","hoja","🥬",
                        m(2,8), 30,
                        m(2,3,8,9),
                        m(3,4,9,10),
                        m(5,6,10,11,12,1,2,3,4),
                        "Cada 7-10 días","mano",
                        l("Retirar hojas externas amarillas","Aclareo a 20 cm","Control de mildiu"),
                        "Muy productiva, cosecha hoja a hoja"),

                new InfoCultivo("Rúcula","hoja","🌿",
                        null, null,
                        m(3,4,5,8,9,10),
                        m(3,4,5,8,9,10),
                        m(4,5,6,9,10,11),
                        "Cada 5-6 días","mano",
                        l("Cosechar antes de florecer","Siembra escalonada cada 3 semanas"),
                        "Lista en 30-40 días desde siembra"),

                new InfoCultivo("Escarola","hoja","🥬",
                        m(7,8), 30,
                        null,
                        m(8,9),
                        m(10,11,12,1),
                        "Cada 7-8 días","mano",
                        l("Atar hojas para blanquear cogollo","Control de pulgón"),
                        "El blanqueado mejora el sabor"),

                new InfoCultivo("Canónigos","hoja","🌿",
                        null, null,
                        m(9,10),
                        m(9,10),
                        m(11,12,1,2,3),
                        "Cada 8-10 días","mano",
                        l("Siembra en líneas muy juntas","Cosechar por manojos"),
                        "Ideal como cultivo de invierno"),

                new InfoCultivo("Kale","hoja","🥬",
                        m(3,4,7,8), 30,
                        null,
                        m(4,5,8,9),
                        m(10,11,12,1,2,3),
                        "Cada 8-10 días","mano",
                        l("Cosechar hojas externas dejando el cogollo","Aguanta heladas leves"),
                        "Las heladas mejoran su sabor"),

                new InfoCultivo("Albahaca","hoja","🌿",
                        m(3,4), 30,
                        null,
                        m(5,6),
                        m(6,7,8,9),
                        "Cada 3-4 días, muy sensible a sequía","mano",
                        l("Eliminar flores para prolongar producción","No mojar hojas","Proteger de frío"),
                        "No soporta temperaturas por debajo de 10°C"),

                new InfoCultivo("Cebolla","bulbo","🧅",
                        m(8,9), 60,
                        null,
                        m(11,12),
                        m(5,6),
                        "Cada 10-12 días, suprimir 2 sem. antes","surco",
                        l("No aporcar","Escarda frecuente","Doblar tallos al madurar"),
                        "Cuando el tallo se dobla es señal de madurez"),

                new InfoCultivo("Ajo","bulbo","🧄",
                        null, null,
                        m(10,11),
                        m(10,11),
                        m(5,6),
                        "2-3 riegos en primavera, suprimir al secar","surco",
                        l("Eliminar escapo floral","No aporcar","Trenzar para guardar"),
                        "Sembrar dientes sanos con piel"),

                new InfoCultivo("Cebolleta","bulbo","🧅",
                        null, null,
                        m(2,3,8,9),
                        m(2,3,8,9),
                        m(4,5,10,11),
                        "Cada 7-8 días","mano",
                        l("Aclareo a 5 cm","Cosechar joven"),
                        "Lista en 60-70 días desde siembra"),

                new InfoCultivo("Zanahoria","raiz","🥕",
                        null, null,
                        m(2,3,4,8,9),
                        m(2,3,4,8,9),
                        m(6,7,8,10,11,12),
                        "Cada 8-10 días, uniforme","mano",
                        l("Aclareo a 5 cm imprescindible","Suelo sin piedras para raíz recta","Escarda"),
                        "Riego irregular provoca grietas en la raíz"),

                new InfoCultivo("Rábano","raiz","🌰",
                        null, null,
                        m(2,3,4,5,8,9,10),
                        m(2,3,4,5,8,9,10),
                        m(3,4,5,6,9,10,11),
                        "Cada 5-6 días","mano",
                        l("Aclareo a 5 cm","Cosechar antes de que abra"),
                        "El más rápido: listo en 25-30 días"),

                new InfoCultivo("Remolacha","raiz","🔴",
                        null, null,
                        m(3,4,5),
                        m(3,4,5),
                        m(7,8,9),
                        "Cada 8-10 días","mano",
                        l("Aclareo a 10 cm","Las hojas también son comestibles"),
                        "Cosechar cuando tiene 5-7 cm de diámetro"),

                new InfoCultivo("Nabo","raiz","⚪",
                        null, null,
                        m(8,9,10),
                        m(8,9,10),
                        m(11,12,1,2),
                        "Cada 8-10 días","mano",
                        l("Aclareo a 15 cm","Control de gusano de alambre"),
                        "Las hojas (nabiza) también se consumen"),

                new InfoCultivo("Chirivía","raiz","🌰",
                        null, null,
                        m(2,3),
                        m(2,3),
                        m(10,11,12,1),
                        "Cada 10-12 días","mano",
                        l("Germinación lenta 2-3 semanas","Suelo profundo y suelto"),
                        "Las heladas mejoran su sabor azucarado"),

                new InfoCultivo("Apio","tallo","🌿",
                        m(2,3), 60,
                        null,
                        m(4,5),
                        m(8,9,10,11),
                        "Cada 5-6 días, muy exigente en agua","mano",
                        l("Aporcar para blanquear los tallos","Riego constante","Control de septoria"),
                        "Necesita suelo muy fértil y húmedo"),

                new InfoCultivo("Puerro","tallo","🌿",
                        m(1,2), 60,
                        null,
                        m(5,6),
                        m(10,11,12,1,2,3),
                        "Cada 8-10 días","mano",
                        l("Aporcar progresivamente para blanquear","Plantar profundo en agujero"),
                        "Aguanta bien las heladas en el suelo"),

                new InfoCultivo("Espárrago","tallo","🌿",
                        m(1,2), 365,
                        null,
                        m(3,4),
                        m(3,4,5),
                        "Cada 12-15 días, suprimir en verano","goteo",
                        l("No cosechar el primer año","Aporcar en invierno para blanqueo","Dejar secar en verano"),
                        "A partir del 3er año, cosecha durante 6-8 semanas"),

                new InfoCultivo("Alcachofa","otro","🌸",
                        m(7,8), 0,
                        null,
                        m(8,9,10),
                        m(11,12,1,2,3,4),
                        "Cada 7-10 días en seco","surco",
                        l("Eliminar hijuelos dejando 2-3 por cepa","Eliminar escapos florales tardíos","Cortar capítulos antes de abrir"),
                        "Planta vivaz, puede durar 5-6 años"),

                new InfoCultivo("Brócoli","col","🥦",
                        m(7,8), 35,
                        null,
                        m(8,9,10),
                        m(11,12,1,2,3),
                        "Cada 8-10 días","mano o aspersión",
                        l("Retirar hojas basales amarillas","Control de oruga de la col","Cosechar pella antes de florecer"),
                        "Tras la pella central brotan pellas laterales"),

                new InfoCultivo("Coliflor","col","🥦",
                        m(7,8), 35,
                        null,
                        m(8,9),
                        m(12,1,2,3),
                        "Cada 8-10 días","mano",
                        l("Doblar hojas sobre la pella para blanquear","Control de Pieris rapae"),
                        "La pella se daña con el sol directo"),

                new InfoCultivo("Col repollo","col","🥬",
                        m(7,8), 35,
                        null,
                        m(8,9),
                        m(11,12,1,2),
                        "Cada 8-10 días","mano",
                        l("Control de oruga","Retirar hojas externas","Aporcar ligeramente"),
                        "Aguanta bien las heladas"),

                new InfoCultivo("Col lombarda","col","🟣",
                        m(5,6), 35,
                        null,
                        m(7,8),
                        m(11,12,1),
                        "Cada 8-10 días","mano",
                        l("Control de pulgón ceniciento","No encharcar"),
                        "El color mejora con el frío"),

                new InfoCultivo("Col de Bruselas","col","🟢",
                        m(4,5), 35,
                        null,
                        m(6,7),
                        m(11,12,1,2),
                        "Cada 8-10 días","mano",
                        l("Despuntar ápice en septiembre para uniformizar","Aporcar"),
                        "Cosechar de abajo a arriba"),

                new InfoCultivo("Romanescu","col","🥦",
                        m(6,7), 35,
                        null,
                        m(7,8),
                        m(11,12),
                        "Cada 8-10 días","mano",
                        l("Similar a la coliflor","Control de plagas de cole"),
                        "El más vistoso de las coles"),

                new InfoCultivo("Patata","tuberculo","🥔",
                        null, null,
                        m(2,3,4),
                        m(2,3,4),
                        m(6,7,8),
                        "Cada 8-10 días, crítico en tuberización","aspersión",
                        l("Aporcar al llegar a 20 cm","Tratamiento contra mildiu","Tratamiento escarabajo"),
                        "No regar en exceso al engordar el tubérculo"),

                new InfoCultivo("Boniato","tuberculo","🍠",
                        m(3,4), 0,
                        null,
                        m(5,6),
                        m(9,10,11),
                        "Cada 8-10 días, reducir al madurar","mano",
                        l("Acolchar suelo","Control de gusano blanco","Cosechar antes de heladas"),
                        "Propagar por esquejes de la planta madre"),

                new InfoCultivo("Topinambur","tuberculo","🌻",
                        null, null,
                        m(2,3),
                        m(2,3),
                        m(11,12,1,2),
                        "Lluvia natural + ocasional en seco","mano",
                        l("Invasivo: usar recipiente o border","Cosechar tras primeras heladas"),
                        "Sin cuidados, muy rústico"),

                new InfoCultivo("Chufa","otro","🌾",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(11,12),
                        "Cada 5-7 días, abundante","surco",
                        l("Suelo arenoso y suelto","Cosechar tras secar hojas","Lavar y secar antes de consumir"),
                        "Exclusiva de Valencia — IGP Chufa de Valencia"),

                new InfoCultivo("Maíz dulce","otro","🌽",
                        null, null,
                        m(4,5),
                        m(4,5),
                        m(7,8,9),
                        "Cada 5-7 días, abundante en espigado","aspersión",
                        l("Plantar en bloque para polinización","Eliminar rebrotes laterales","Cosechar con leche en el grano"),
                        "La mazorca está lista cuando los pelos se secan"),

                new InfoCultivo("Hinojo","otro","🌿",
                        m(3,4,8), 20,
                        null,
                        m(4,5,9),
                        m(7,8,10,11),
                        "Cada 8-10 días","mano",
                        l("Aporcar el bulbo para blanquear","No dejar florecer"),
                        "Las hojas también son aromáticas"),

                new InfoCultivo("Fresa","otro","🍓",
                        m(7,8), 0,
                        null,
                        m(9,10),
                        m(1,2,3,4,5),
                        "Cada 3-4 días, goteo bajo acolchado","goteo",
                        l("Acolchar con plástico negro","Eliminar estolones","Control de botrytis"),
                        "Renovar plantel cada 3-4 años"),

                new InfoCultivo("Okra","fruto","🌿",
                        m(3,4), 20,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 4-5 días, muy exigente en calor","goteo",
                        l("Cosechar cada 2-3 días antes de lignificar","Usar guantes — pelillos irritantes"),
                        "Necesita más de 20°C para germinar")
        );
    }

    // ─────────────────────────────────────────────
    // ZONA CONTINENTAL
    // ─────────────────────────────────────────────
    private List<InfoCultivo> catalogoContinental() {
        return List.of(

                new InfoCultivo("Tomate","fruto","🍅",
                        m(2,3), 55,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 4-5 días en verano","goteo",
                        l("Entutorar","Eliminar chupones","Control de mildiu"),
                        "Trasplantar tras últimas heladas (mayo en interior)"),

                new InfoCultivo("Tomate industrial","fruto","🍅",
                        null, null,
                        m(5), null,
                        m(8,9),
                        "Goteo o aspersión, 400-600 mm en ciclo","goteo",
                        l("Sin entutorar","Control de alternaria","Cosecha mecánica"),
                        "Extremadura — primer productor europeo"),

                new InfoCultivo("Pimiento","fruto","🫑",
                        m(2,3), 60,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 5-6 días","goteo",
                        l("Entutorar","Acolchar suelo"),
                        "Riesgo de heladas tardías en mayo en altura"),

                new InfoCultivo("Pimiento para pimentón","fruto","🌶️",
                        m(2,3), 60,
                        null,
                        m(5),
                        m(9,10),
                        "Cada 5-7 días","goteo",
                        l("Entutorado en espaldera","Aporcado","Secado en ahumadero"),
                        "IGP Pimentón de la Vera — Extremadura"),

                new InfoCultivo("Berenjena","fruto","🍆",
                        m(2,3), 60,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 5 días, constante","goteo",
                        l("Entutorar","Control de araña roja"),
                        "Necesita veranos cálidos — zona sur continental"),

                new InfoCultivo("Pepino","fruto","🥒",
                        m(3,4), 20,
                        null,
                        m(5,6),
                        m(7,8,9),
                        "Cada 4-5 días","goteo",
                        l("Entutorar","Deshojado basal"),
                        "Sensible a heladas tardías"),

                new InfoCultivo("Calabacín","fruto","🥬",
                        m(4,5), 15,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 5-6 días","mano",
                        l("Polinización manual","Retirar hojas viejas"),
                        "Ciclo más corto que en Mediterráneo"),

                new InfoCultivo("Calabaza","fruto","🎃",
                        m(4,5), 20,
                        null,
                        m(5,6),
                        m(9,10),
                        "Cada 7-8 días","surco",
                        l("Dejar 2-3 frutos","Cura en seco"),
                        "Aguanta en bodega todo el invierno"),

                new InfoCultivo("Melón","fruto","🍈",
                        m(3,4), 30,
                        null,
                        m(5),
                        m(7,8,9),
                        "Cada 7-8 días, muy escaso al madurar","surco",
                        l("Pellizcar guías","Dejar 2-3 frutos"),
                        "Villaconejos (Madrid) y La Mancha — zona clásica"),

                new InfoCultivo("Sandía","fruto","🍉",
                        m(3,4), 30,
                        null,
                        m(5),
                        m(7,8,9),
                        "Cada 6-7 días, suprimir al madurar","surco",
                        l("Acolchar suelo","Aclarar frutos"),
                        "Necesita veranos muy cálidos"),

                new InfoCultivo("Judía verde","legumbre","🫘",
                        null, null,
                        m(5,6),
                        m(5,6),
                        m(7,8,9),
                        "Cada 5-6 días en floración","mano",
                        l("Entutorar trepadoras","No mojar en floración"),
                        "Un solo ciclo en zona fría"),

                new InfoCultivo("Judía de El Barco de Ávila","legumbre","🫘",
                        null, null,
                        m(5,6),
                        m(5,6),
                        m(9,10),
                        "Cada 6-8 días en floración","mano",
                        l("Entutorar en cañas","Cosechar seca"),
                        "IGP — producto estrella de Ávila"),

                new InfoCultivo("Habas","legumbre","🫘",
                        null, null,
                        m(10,11),
                        m(10,11),
                        m(4,5),
                        "Lluvia natural + puntual","mano",
                        l("Despuntar ápice contra pulgón","Apuntalar"),
                        "Aguanta heladas leves"),

                new InfoCultivo("Guisante","legumbre","🟢",
                        null, null,
                        m(10,11,2,3),
                        m(10,11,2,3),
                        m(4,5),
                        "Lluvia + riego puntual","mano",
                        l("Entutorar en red","Cosechar joven"),
                        "No tolera calor veraniego"),

                new InfoCultivo("Garbanzo verde","legumbre","🟡",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(6,7),
                        "Muy escaso, 2-3 riegos","mano",
                        l("Suelo bien drenado","No encharcar nunca"),
                        "Zona Castilla — gran tradición"),

                new InfoCultivo("Lechuga","hoja","🥬",
                        m(1,2,8,9), 30,
                        null,
                        m(3,4,9,10),
                        m(5,6,11,12,1),
                        "Cada 6-8 días","mano",
                        l("Control de babosas","Escarda"),
                        "Dos ciclos: primavera y otoño"),

                new InfoCultivo("Espinaca","hoja","🌿",
                        null, null,
                        m(2,3,8,9),
                        m(2,3,8,9),
                        m(4,5,10,11),
                        "Cada 8-10 días","mano",
                        l("Aclareo a 10 cm","No dejar espolar"),
                        "Se va a semilla con calor estival"),

                new InfoCultivo("Acelga","hoja","🥬",
                        m(2,8), 30,
                        m(2,3,8,9),
                        m(3,4,9,10),
                        m(5,6,10,11,12,1),
                        "Cada 8-10 días","mano",
                        l("Retirar hojas amarillas","Aclareo a 20 cm"),
                        "Muy resistente al frío"),

                new InfoCultivo("Borraja","hoja","🌿",
                        null, null,
                        m(3,4,5,8,9),
                        m(3,4,5,8,9),
                        m(5,6,7,10,11),
                        "Cada 7-8 días","mano",
                        l("Cosechar tallos tiernos","Usar guantes — tiene pelillos"),
                        "Aragón — producto estrella regional"),

                new InfoCultivo("Cardo","tallo","🌿",
                        m(5,6), 30,
                        null,
                        m(6,7),
                        m(12,1),
                        "Cada 8-10 días","surco",
                        l("Atar y cubrir para blanquear","Control de pulgón"),
                        "Navarra y Aragón — plato navideño tradicional"),

                new InfoCultivo("Kale","hoja","🥬",
                        m(4,5,7,8), 30,
                        null,
                        m(5,6,8,9),
                        m(10,11,12,1,2,3),
                        "Cada 8-10 días","mano",
                        l("Cosechar hojas externas","Aguanta heladas fuertes"),
                        "Las heladas mejoran su sabor"),

                new InfoCultivo("Cebolla","bulbo","🧅",
                        m(1,2), 60,
                        null,
                        m(3,4),
                        m(7,8),
                        "Cada 10-12 días en verano","surco",
                        l("Escarda frecuente","Doblar tallos al madurar"),
                        "Curado en secadero antes de guardar"),

                new InfoCultivo("Ajo morado Las Pedroñeras","bulbo","🧄",
                        null, null,
                        m(10,11),
                        m(10,11),
                        m(6,7),
                        "Muy escaso: 2-3 riegos primaverales","surco",
                        l("Eliminar escapo floral","Trenzar para guardar"),
                        "IGP — el ajo más famoso de España"),

                new InfoCultivo("Ajo","bulbo","🧄",
                        null, null,
                        m(10,11),
                        m(10,11),
                        m(5,6),
                        "2-3 riegos en primavera","surco",
                        l("No aporcar","Trenzar para curar"),
                        "Sembrar diente orientado hacia arriba"),

                new InfoCultivo("Zanahoria","raiz","🥕",
                        null, null,
                        m(3,4,5),
                        m(3,4,5),
                        m(7,8,9),
                        "Cada 8-10 días, uniforme","mano",
                        l("Aclareo imprescindible","Suelo profundo sin piedras"),
                        "Riego irregular → raíz agrietada"),

                new InfoCultivo("Remolacha","raiz","🔴",
                        null, null,
                        m(3,4,5),
                        m(3,4,5),
                        m(7,8,9),
                        "Cada 8-10 días","mano",
                        l("Aclareo a 10 cm","Las hojas también son comestibles"),
                        "Cosechar a 5-7 cm de diámetro"),

                new InfoCultivo("Patata","tuberculo","🥔",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(7,8),
                        "Cada 8-10 días, crítico en tuberización","aspersión",
                        l("Aporcar 2 veces","Control mildiu y escarabajo"),
                        "No regar en exceso al engordar"),

                new InfoCultivo("Patata violeta","tuberculo","🥔",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(8,9),
                        "Cada 8-10 días","aspersión",
                        l("Igual que la patata común","Conserva mejor en bodega"),
                        "Mayor contenido en antioxidantes"),

                new InfoCultivo("Nabo","raiz","⚪",
                        null, null,
                        m(8,9,10),
                        m(8,9,10),
                        m(11,12,1),
                        "Cada 8-10 días","mano",
                        l("Aclareo a 15 cm"),
                        "Las nabizas (hojas) son comestibles"),

                new InfoCultivo("Puerro","tallo","🌿",
                        m(1,2), 60,
                        null,
                        m(4,5,6),
                        m(10,11,12,1,2),
                        "Cada 8-10 días","mano",
                        l("Aporcar para blanquear","Plantar en agujero profundo"),
                        "Aguanta heladas en el suelo"),

                new InfoCultivo("Espárrago","tallo","🌿",
                        m(1,2), 365,
                        null,
                        m(3,4),
                        m(3,4,5),
                        "Cada 12-15 días","goteo",
                        l("No cosechar año 1","Aporcar en invierno","Dejar secar en verano"),
                        "Espárrago blanco — tapa con tierra los turiones"),

                new InfoCultivo("Brócoli","col","🥦",
                        m(7,8), 35,
                        null,
                        m(8,9),
                        m(11,12,1,2),
                        "Cada 8-10 días","mano",
                        l("Control oruga","Cosechar pella antes de abrir"),
                        "Brotan pellas laterales tras la central"),

                new InfoCultivo("Coliflor","col","🥦",
                        m(7,8), 35,
                        null,
                        m(8,9),
                        m(12,1,2),
                        "Cada 8-10 días","mano",
                        l("Doblar hojas sobre pella","Control Pieris rapae"),
                        "La pella se daña con sol directo"),

                new InfoCultivo("Col repollo","col","🥬",
                        m(7,8), 35,
                        null,
                        m(8,9),
                        m(11,12,1),
                        "Cada 8-10 días","mano",
                        l("Control de oruga","Aporcar ligeramente"),
                        "Aguanta heladas fuertes"),

                new InfoCultivo("Rábano","raiz","🌰",
                        null, null,
                        m(3,4,5,8,9),
                        m(3,4,5,8,9),
                        m(4,5,6,9,10),
                        "Cada 5-6 días","mano",
                        l("Aclareo a 5 cm","Cosechar antes de que abra"),
                        "Listo en 25-30 días"),

                new InfoCultivo("Maíz dulce","otro","🌽",
                        null, null,
                        m(5),
                        m(5),
                        m(8,9),
                        "Cada 5-7 días, abundante en espigado","aspersión",
                        l("Plantar en bloque","Eliminar rebrotes"),
                        "Cosechar cuando los pelos se secan"),

                new InfoCultivo("Topinambur","tuberculo","🌻",
                        null, null,
                        m(2,3),
                        m(2,3),
                        m(11,12,1),
                        "Lluvia natural, muy rústico","mano",
                        l("Usar recipiente — es invasivo","Cosechar tras heladas"),
                        "Sin apenas cuidados")
        );
    }

    // ─────────────────────────────────────────────
    // ZONA ATLÁNTICA
    // ─────────────────────────────────────────────
    private List<InfoCultivo> catalogoAtlantico() {
        return List.of(

                new InfoCultivo("Patata de Galicia","tuberculo","🥔",
                        null, null,
                        m(3,4,5),
                        m(3,4,5),
                        m(7,8,9),
                        "Lluvia natural + aspersión en seco","aspersión",
                        l("Aporcar 2 veces","Control mildiu y escarabajo","Pregerminación en lugar luminoso"),
                        "IGP Pataca de Galicia — la más apreciada"),

                new InfoCultivo("Grelo","hoja","🌿",
                        null, null,
                        m(8,9),
                        m(8,9),
                        m(11,12,1,2,3),
                        "Lluvia natural","mano",
                        l("Aclareo a 15-20 cm","Cosechar hoja tierna antes de florecer"),
                        "Hoja del nabo — plato icónico gallego"),

                new InfoCultivo("Col gallega / Berza","hoja","🥬",
                        m(5,6), 30,
                        null,
                        m(7,8),
                        m(10,11,12,1,2,3),
                        "Lluvia natural","mano",
                        l("Aporcar ligeramente","Retirar hojas basales","Control de oruga"),
                        "Cosecha hoja a hoja durante meses"),

                new InfoCultivo("Nabo gallego","raiz","⚪",
                        null, null,
                        m(8,9),
                        m(8,9),
                        m(11,12,1),
                        "Lluvia natural","mano",
                        l("Aclareo a 15 cm"),
                        "Las nabizas (hojas) — apertura de la temporada gallega"),

                new InfoCultivo("Cebolla de Betanzos","bulbo","🧅",
                        m(2,3), 60,
                        null,
                        m(5),
                        m(7,8),
                        "Lluvia natural + riego puntual en seco","mano",
                        l("Escarda","No aporcar","Secar en campo antes de guardar"),
                        "IGP — cebolla dulce muy apreciada"),

                new InfoCultivo("Pimiento de Gernika","fruto","🫑",
                        m(2,3), 55,
                        null,
                        m(5),
                        m(8,9,10),
                        "Cada 5-7 días","goteo o mano",
                        l("Entutorar","Eliminar chupones basales"),
                        "IGP — se consume verde, frito en aceite"),

                new InfoCultivo("Pimiento del Piquillo","fruto","🌶️",
                        m(2,3), 55,
                        null,
                        m(5),
                        m(9,10),
                        "Cada 5-7 días","goteo",
                        l("Entutorado en espaldera","Aclareo de frutos"),
                        "IGP Lodosa — se asa y conserva en tarro"),

                new InfoCultivo("Espárrago de Navarra","tallo","🌿",
                        m(1,2), 365,
                        null,
                        m(3,4),
                        m(4,5,6),
                        "Cada 10-12 días","goteo",
                        l("No cosechar año 1","Aporcar para blanqueo","Cosechar antes de que abran las escamas"),
                        "IGP — el espárrago más reconocido de España"),

                new InfoCultivo("Alcachofa de Tudela","otro","🌸",
                        m(7,8), 0,
                        null,
                        m(9),
                        m(11,12,1,2,3,4),
                        "Cada 7-8 días","surco",
                        l("Eliminar hijuelos","Dejar 3 por cepa"),
                        "Navarra — variedad muy tierna y sin espinas"),

                new InfoCultivo("Judía verdina de Asturias","legumbre","🫘",
                        null, null,
                        m(5),
                        m(5),
                        m(9,10),
                        "Lluvia natural + puntual en seco","mano",
                        l("Entutorar en varas","Cosechar en verde"),
                        "Característica por su color verde al cocerla"),

                new InfoCultivo("Faba asturiana","legumbre","⚪",
                        null, null,
                        m(5),
                        m(5),
                        m(9,10),
                        "Lluvia natural + eventual en seco","mano",
                        l("Entutorar alto","Cosechar seca en la vaina"),
                        "IGP — base de la fabada asturiana"),

                new InfoCultivo("Guisante lágrima","legumbre","🟢",
                        null, null,
                        m(11,12,1,2),
                        m(11,12,1,2),
                        m(4,5),
                        "Lluvia natural","mano",
                        l("Entutorar en red","Cosechar muy joven — grano pequeño y dulce"),
                        "País Vasco — producto gourmet de temporada corta"),

                new InfoCultivo("Judía de Tolosa","legumbre","🟤",
                        null, null,
                        m(5,6),
                        m(5,6),
                        m(10,11),
                        "Lluvia natural + puntual","mano",
                        l("Entutorar alto","Cosechar seca"),
                        "IGP — la judía negra más valorada del norte"),

                new InfoCultivo("Col repollo","col","🥬",
                        m(5,6), 35,
                        null,
                        m(7,8),
                        m(10,11,12,1),
                        "Lluvia natural + puntual","mano",
                        l("Control de oruga","Retirar hojas externas"),
                        "Aguanta heladas, mejora con frío"),

                new InfoCultivo("Col de Bruselas","col","🟢",
                        m(4,5), 35,
                        null,
                        m(6,7),
                        m(11,12,1,2),
                        "Lluvia natural + puntual","mano",
                        l("Despuntar en septiembre","Aporcar"),
                        "Cosechar de abajo a arriba"),

                new InfoCultivo("Brócoli","col","🥦",
                        m(6,7), 35,
                        null,
                        m(7,8),
                        m(10,11,12),
                        "Lluvia + riego puntual","mano",
                        l("Control oruga de la col","Cosechar pella antes de abrir"),
                        "Pellas laterales tras la central"),

                new InfoCultivo("Coliflor","col","🥦",
                        m(6,7), 35,
                        null,
                        m(7,8),
                        m(11,12,1),
                        "Lluvia + puntual","mano",
                        l("Doblar hojas para blanquear"),
                        "Zona más fresca — ciclo más largo"),

                new InfoCultivo("Puerro","tallo","🌿",
                        m(2,3), 60,
                        null,
                        m(5,6),
                        m(10,11,12,1,2),
                        "Lluvia natural + puntual","mano",
                        l("Aporcar para blanquear","Plantar profundo"),
                        "Muy resistente al frío y a la humedad"),

                new InfoCultivo("Zanahoria","raiz","🥕",
                        null, null,
                        m(3,4,5),
                        m(3,4,5),
                        m(7,8,9),
                        "Lluvia + puntual en seco","mano",
                        l("Aclareo a 5 cm","Suelo suelto y profundo"),
                        "Riego irregular → raíces bifurcadas"),

                new InfoCultivo("Remolacha","raiz","🔴",
                        null, null,
                        m(4,5),
                        m(4,5),
                        m(8,9,10),
                        "Lluvia + puntual","mano",
                        l("Aclareo a 10 cm","Hojas comestibles"),
                        "Las hojas se consumen como acelga"),

                new InfoCultivo("Cebolleta","bulbo","🧅",
                        null, null,
                        m(3,4,8),
                        m(3,4,8),
                        m(5,6,10),
                        "Lluvia + puntual","mano",
                        l("Aclareo a 5 cm","Cosechar joven"),
                        "Lista en 60-70 días"),

                new InfoCultivo("Lechuga","hoja","🥬",
                        m(3,4,8,9), 30,
                        null,
                        m(4,5,9,10),
                        m(6,7,10,11,12),
                        "Lluvia + puntual","mano",
                        l("Control babosas","No mojar cogollo"),
                        "Plantar a la sombra en verano"),

                new InfoCultivo("Espinaca","hoja","🌿",
                        null, null,
                        m(3,4,8,9),
                        m(3,4,8,9),
                        m(5,6,10,11),
                        "Lluvia + puntual","mano",
                        l("Aclareo a 10 cm","No dejar espolar"),
                        "Clima atlántico muy favorable"),

                new InfoCultivo("Acelga","hoja","🥬",
                        m(3,8), 30,
                        null,
                        m(4,9),
                        m(6,7,10,11,12,1),
                        "Lluvia + puntual","mano",
                        l("Retirar hojas amarillas","Aclareo a 20 cm"),
                        "Muy productiva en clima húmedo"),

                new InfoCultivo("Berro","hoja","🌿",
                        null, null,
                        m(3,4,8,9),
                        m(3,4,8,9),
                        m(5,6,10,11),
                        "Suelo húmedo constante — junto a agua","mano",
                        l("Mantener humedad alta","Cosechar brotes tiernos"),
                        "Crece de forma natural en regatos y bordes de ríos"),

                new InfoCultivo("Kale","hoja","🥬",
                        m(5,6,7), 30,
                        null,
                        m(6,7,8),
                        m(10,11,12,1,2,3),
                        "Lluvia natural + puntual","mano",
                        l("Cosechar hojas externas","Las heladas mejoran el sabor"),
                        "Ideal para el clima atlántico húmedo"),

                new InfoCultivo("Topinambur","tuberculo","🌻",
                        null, null,
                        m(2,3),
                        m(2,3),
                        m(11,12,1),
                        "Lluvia natural — muy rústico","mano",
                        l("Contener la expansión","Cosechar tras heladas"),
                        "Sin cuidados, produce abundantemente"),

                new InfoCultivo("Maíz dulce","otro","🌽",
                        null, null,
                        m(5),
                        m(5),
                        m(8,9),
                        "Lluvia + riego en espigado","aspersión",
                        l("Plantar en bloque","Eliminar rebrotes"),
                        "Floración más tardía que en el sur"),

                new InfoCultivo("Cardo","tallo","🌿",
                        m(4,5), 30,
                        null,
                        m(6,7),
                        m(12,1),
                        "Lluvia + puntual en verano","surco",
                        l("Atar y cubrir para blanquear en otoño","Control pulgón"),
                        "Navarra y Aragón — plato navideño"),

                new InfoCultivo("Nabo","raiz","⚪",
                        null, null,
                        m(8,9),
                        m(8,9),
                        m(11,12,1),
                        "Lluvia natural","mano",
                        l("Aclareo a 15 cm","Nabizas comestibles"),
                        "Muy adaptado al clima húmedo del norte"),

                new InfoCultivo("Rábano","raiz","🌰",
                        null, null,
                        m(3,4,5,8,9),
                        m(3,4,5,8,9),
                        m(4,5,6,9,10),
                        "Lluvia + puntual","mano",
                        l("Aclareo a 5 cm","Cosechar antes de que abra"),
                        "El cultivo más rápido del huerto")
        );
    }

    // ─────────────────────────────────────────────
    // ZONA SUBTROPICAL
    // ─────────────────────────────────────────────
    private List<InfoCultivo> catalogoSubtropical() {
        return List.of(

                new InfoCultivo("Tomate canario","fruto","🍅",
                        m(7,8), 50,
                        null,
                        m(9,10),
                        m(12,1,2,3,4,5),
                        "Goteo cada 1-2 días (agua desalada)","goteo",
                        l("Entutorado en rafia","Despunte","Control mosca blanca"),
                        "Ciclo inverso al peninsular — producción en invierno"),

                new InfoCultivo("Tomate cherry","fruto","🍒",
                        m(7,8), 50,
                        null,
                        m(9,10),
                        m(12,1,2,3,4,5),
                        "Goteo cada 2 días","goteo",
                        l("Entutorar","Cosechar por racimos"),
                        "Muy productivo en clima cálido"),

                new InfoCultivo("Pimiento","fruto","🫑",
                        m(7,8), 55,
                        null,
                        m(9,10),
                        m(12,1,2,3,4,5,6),
                        "Goteo cada 2-3 días","goteo",
                        l("Entutorado","Poda de formación en Y"),
                        "Producción casi todo el año en Canarias"),

                new InfoCultivo("Pimiento padrón","fruto","🌶️",
                        m(7,8), 55,
                        null,
                        m(9,10),
                        m(12,1,2,3,4,5),
                        "Goteo cada 2-3 días","goteo",
                        l("Cosechar verde y pequeño","Freír en aceite"),
                        "Adaptado a Canarias y costa tropical"),

                new InfoCultivo("Pepino","fruto","🥒",
                        m(8,9), 20,
                        null,
                        m(10),
                        m(12,1,2,3,4,5),
                        "Goteo cada 1-2 días","goteo",
                        l("Entutorar","Deshojado","Control araña roja"),
                        "Ciclo otoño-primavera en Canarias"),

                new InfoCultivo("Calabacín","fruto","🥬",
                        m(8,9), 15,
                        null,
                        m(10),
                        m(12,1,2,3,4),
                        "Goteo cada 2-3 días","goteo",
                        l("Polinización manual","Retirar hojas viejas"),
                        "Alta producción en invierno canario"),

                new InfoCultivo("Berenjena","fruto","🍆",
                        m(7,8), 60,
                        null,
                        m(9,10),
                        m(12,1,2,3,4,5),
                        "Goteo cada 2-3 días","goteo",
                        l("Entutorar","Poda rejuvenecimiento"),
                        "El calor favorece la producción"),

                new InfoCultivo("Sandía","fruto","🍉",
                        m(2,3), 30,
                        null,
                        m(4,5),
                        m(7,8,9),
                        "Cada 5-6 días, suprimir al madurar","goteo",
                        l("Aclarar a 2 frutos","Acolchar suelo"),
                        "Dos ciclos posibles en zonas muy cálidas"),

                new InfoCultivo("Melón","fruto","🍈",
                        m(2,3), 30,
                        null,
                        m(4,5),
                        m(7,8,9),
                        "Cada 6-7 días, reducir al madurar","surco",
                        l("Pellizcar guías","Dejar 2-3 frutos"),
                        "El calor constante favorece el dulzor"),

                new InfoCultivo("Chayote","fruto","🥬",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(9,10,11,12),
                        "Cada 5-6 días","mano",
                        l("Emparrar sobre estructura alta","Muy productivo — cosechar joven"),
                        "Canarias y costa tropical — produce decenas de frutos"),

                new InfoCultivo("Judía verde","legumbre","🫘",
                        null, null,
                        m(8,9,10),
                        m(8,9,10),
                        m(11,12,1,2,3,4),
                        "Cada 3-4 días","mano o goteo",
                        l("Entutorar","Cosechar frecuentemente"),
                        "Producción en invierno — al revés que en península"),

                new InfoCultivo("Judía carilla","legumbre","⚫",
                        null, null,
                        m(4,5),
                        m(4,5),
                        m(8,9,10),
                        "Cada 7-8 días","mano",
                        l("Muy rústica","Tolera sequía","Cosechar seca"),
                        "Producto estrella de Canarias — rancho canario"),

                new InfoCultivo("Batata canaria","tuberculo","🍠",
                        m(3,4), 0,
                        null,
                        m(5,6),
                        m(9,10,11),
                        "Cada 7-8 días","mano",
                        l("Acolchar suelo","Control gusano blanco","Cosechar antes de lluvias fuertes"),
                        "Canarias — producción notable en Gran Canaria"),

                new InfoCultivo("Papa bonita","tuberculo","🥔",
                        null, null,
                        m(2,3,9,10),
                        m(2,3,9,10),
                        m(6,7,1,2),
                        "Cada 7-8 días","aspersión",
                        l("Aporcar","Control mildiu"),
                        "IGP Papas Antiguas de Canarias — 2 ciclos al año"),

                new InfoCultivo("Papa negra","tuberculo","🥔",
                        null, null,
                        m(2,3),
                        m(2,3),
                        m(6,7),
                        "Cada 7-8 días","aspersión",
                        l("Aporcar","Sin productos químicos generalmente"),
                        "Variedad autóctona de piel negra y sabor intenso"),

                new InfoCultivo("Ñame","tuberculo","🌰",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(10,11,12),
                        "Cada 8-10 días","mano",
                        l("Emparrar — planta trepadora","Suelo profundo y fértil","Cosechar tras secar hojas"),
                        "Canarias — tubérculo tropical de gran tamaño"),

                new InfoCultivo("Malanga / Taro","tuberculo","🌿",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(10,11,12),
                        "Cada 7-8 días, suelo húmedo","mano",
                        l("Suelo muy fértil","Control de caracoles","Cosechar antes de frío"),
                        "Producto tradicional canario — base del rancho"),

                new InfoCultivo("Lechuga","hoja","🥬",
                        m(7,8,1,2), 25,
                        null,
                        m(8,9,2,3),
                        m(10,11,12,4,5),
                        "Cada 3-4 días","goteo",
                        l("Control de trips","Acolchado plástico","Siembra escalonada cada 3 semanas"),
                        "Producción casi continua en clima subtropical"),

                new InfoCultivo("Espinaca","hoja","🌿",
                        null, null,
                        m(9,10,1,2),
                        m(9,10,1,2),
                        m(11,12,1,3,4),
                        "Cada 5-6 días","mano",
                        l("Aclareo a 10 cm","No dejar espolar"),
                        "El calor la hace espigar — sembrar en temporadas frescas"),

                new InfoCultivo("Acelga","hoja","🥬",
                        m(8,9), 25,
                        null,
                        m(9,10),
                        m(11,12,1,2,3,4),
                        "Cada 5-6 días","mano",
                        l("Retirar hojas amarillas","Aclareo"),
                        "Muy productiva en invierno subtropical"),

                new InfoCultivo("Kale","hoja","🥬",
                        m(8,9), 30,
                        null,
                        m(9,10),
                        m(11,12,1,2,3),
                        "Cada 6-7 días","mano",
                        l("Cosechar hojas externas","Muy resistente al calor moderado"),
                        "Se adapta bien al clima canario"),

                new InfoCultivo("Pak choi","hoja","🥬",
                        null, null,
                        m(9,10,1,2),
                        m(9,10,1,2),
                        m(11,12,2,3,4),
                        "Cada 4-5 días","mano",
                        l("Ciclo muy corto: 45 días","Control de caracoles"),
                        "Ideal como cultivo rápido de invierno"),

                new InfoCultivo("Rúcula","hoja","🌿",
                        null, null,
                        m(9,10,1,2,3),
                        m(9,10,1,2,3),
                        m(11,12,1,2,3,4,5),
                        "Cada 4-5 días","mano",
                        l("Cosechar antes de florecer","Siembra escalonada"),
                        "Lista en 30-35 días"),

                new InfoCultivo("Cebolla canaria","bulbo","🧅",
                        m(1,2,7,8), 50,
                        null,
                        m(3,4,9,10),
                        m(6,7,12,1),
                        "Cada 8-10 días","mano",
                        l("Escarda frecuente","No encharcar al madurar"),
                        "Cebolla muy dulce — dos ciclos anuales"),

                new InfoCultivo("Ajo","bulbo","🧄",
                        null, null,
                        m(10,11),
                        m(10,11),
                        m(3,4),
                        "Muy escaso — 2-3 riegos","surco",
                        l("No aporcar","Trenzar para curar"),
                        "Ciclo más corto que en la península"),

                new InfoCultivo("Zanahoria","raiz","🥕",
                        null, null,
                        m(9,10,1),
                        m(9,10,1),
                        m(12,1,4,5),
                        "Cada 6-7 días, uniforme","mano",
                        l("Aclareo a 5 cm","Suelo suelto"),
                        "Siembra en otoño-invierno en Canarias"),

                new InfoCultivo("Remolacha","raiz","🔴",
                        null, null,
                        m(9,10),
                        m(9,10),
                        m(1,2,3),
                        "Cada 7-8 días","mano",
                        l("Aclareo a 10 cm","Hojas comestibles"),
                        "Siembra otoñal en zonas subtropicales"),

                new InfoCultivo("Jengibre","raiz","🌿",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(10,11,12),
                        "Cada 5-7 días, mantener humedad","mano",
                        l("Suelo muy fértil y húmedo","Sombra parcial en verano","Cosechar tras secar hojas"),
                        "Costa tropical de Málaga y Granada — en expansión"),

                new InfoCultivo("Cúrcuma","raiz","🟡",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(10,11),
                        "Cada 6-7 días","mano",
                        l("Suelo fértil","Sombra parcial","Cosechar y secar al sol"),
                        "Costa tropical — se seca y muele como especia"),

                new InfoCultivo("Okra","fruto","🌿",
                        m(3,4), 20,
                        null,
                        m(5,6),
                        m(7,8,9,10),
                        "Cada 4-5 días","goteo",
                        l("Cosechar cada 2-3 días","Usar guantes — pelos irritantes"),
                        "Necesita temperaturas superiores a 20°C"),

                new InfoCultivo("Maíz dulce","otro","🌽",
                        null, null,
                        m(3,4),
                        m(3,4),
                        m(6,7),
                        "Cada 5-6 días en espigado","aspersión",
                        l("Plantar en bloque","Eliminar rebrotes"),
                        "Ciclo primavera-verano en Canarias"),

                new InfoCultivo("Hinojo","otro","🌿",
                        m(8,9), 20,
                        null,
                        m(9,10),
                        m(12,1,2),
                        "Cada 7-8 días","mano",
                        l("Aporcar el bulbo","No dejar florecer"),
                        "Cultivo otoñal en clima subtropical")
        );
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private List<Integer> m(int... meses) {
        List<Integer> l = new ArrayList<>();
        for (int mes : meses) l.add(mes);
        return l;
    }

    private List<String> l(String... items) {
        return List.of(items);
    }

    private double parsearDouble(String valor, double defecto) {
        try {
            return Double.parseDouble(valor.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return defecto;
        }
    }
}


