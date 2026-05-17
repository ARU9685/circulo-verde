# 🌿 Círculo Verde

> Aplicación web inteligente para la gestión de huertos urbanos por zonas climáticas.

![Logo](src/main/resources/static/img/LogoCirculoVerde.jpeg)

---

## 📋 Descripción

**Círculo Verde** es una aplicación web desarrollada como proyecto final de ciclo formativo. Permite a cualquier usuario gestionar su huerto personal de forma inteligente, adaptando las recomendaciones de cultivo, siembra y cuidados según la zona climática de su ciudad.

Integra datos meteorológicos en tiempo real de **OpenWeatherMap** y **AEMET**, recomendaciones agrícolas de **Sativum**, y autenticación con **Google OAuth2**.

---

## ✨ Funcionalidades

### 🔐 Autenticación y seguridad
- Registro con verificación de email, contraseña segura y zona climática automática
- Inicio de sesión con usuario/contraseña (BCrypt) o con **Google OAuth2**
- Protección anti-bot: honeypot + medición de tiempo de formulario
- Sesiones seguras con Spring Security

### 🏡 Panel principal
- Saludo personalizado con ciudad y zona climática
- Clima en tiempo real (temperatura, humedad, viento)
- Próxima tarea pendiente
- Contadores de inventario y diario
- Consejos y cultivos recomendados para la zona

### 📅 Calendario inteligente
- Vista mensual con navegación entre meses
- Tareas personales con colores por tipo (riego, siembra, abonado, mantenimiento)
- Edición y eliminación de tareas directamente desde el calendario
- **Alertas agrícolas AEMET** en tiempo real: heladas, calor extremo, lluvia, viento fuerte
- Pronóstico meteorológico de 7 días (AEMET)
- Cultivos recomendados por mes y zona climática con información completa:
    - Fechas de semillero y días en semillero
    - Fechas de plantación y cosecha
    - Frecuencia y tipo de riego
    - Cuidados y mantenimiento
    - Notas y consejos específicos
- Plagas y fertilizantes recomendados (Sativum)

### 📦 Inventario
- Gestión de semillas, herramientas, fertilizantes y suministros
- Categorías, cantidades y estado (disponible / stock bajo / agotado)
- Edición y eliminación con verificación de pertenencia al usuario

### 📓 Diario del huerto
- Entradas con fecha y contenido libre
- Seguimiento personal del progreso del huerto

### 📊 Estadísticas
- Gráfico de líneas: tareas registradas en los últimos 6 meses (Chart.js)
- Gráfico de dona: distribución del inventario por categoría
- Contadores de tareas, productos y entradas del diario

---

## 🗺️ Zonas climáticas

La app adapta automáticamente todas las recomendaciones según la zona climática detectada por ciudad:

| Zona | Ciudades principales |
|------|---------------------|
| 🌞 Mediterránea | Valencia, Alicante, Murcia, Sevilla, Barcelona, Málaga |
| ❄️ Continental | Madrid, Zaragoza, Toledo, Valladolid, Burgos, Salamanca |
| 🌧️ Atlántica | Bilbao, Santander, A Coruña, Oviedo, Vigo, Pamplona |
| 🌴 Subtropical | Las Palmas, Santa Cruz de Tenerife, Arrecife |

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| **Java** | 21 | Lógica de negocio |
| **Spring Boot** | 3.2.2 | Backend y controladores |
| **Spring Security** | 6 | Autenticación y autorización |
| **Spring Data JPA** | — | Acceso a datos |
| **Thymeleaf** | — | Plantillas HTML |
| **Bootstrap** | 5.3 | Diseño responsive |
| **Chart.js** | 4.4 | Gráficos estadísticos |
| **MySQL** | 8.0 | Base de datos |
| **Maven** | 3.9 | Gestión de dependencias |
| **Docker** | — | Contenedorización |
| **Lombok** | — | Reducción de boilerplate |

---

## 🔌 APIs integradas

| API | Uso |
|-----|-----|
| [OpenWeatherMap](https://openweathermap.org) | Clima actual en tiempo real |
| [AEMET OpenData](https://opendata.aemet.es) | Pronóstico 7 días + alertas agrícolas |
| [Sativum](https://sativum.com) | Plagas y fertilizantes recomendados |
| [Google OAuth2](https://developers.google.com) | Inicio de sesión con Google |

---

## 🚀 Despliegue

La aplicación está contenedorizada con **Docker** y lista para desplegar en AWS EC2.

### Requisitos
- Docker y Docker Compose instalados
- Cuenta en OpenWeatherMap y AEMET (gratuitas)
- Credenciales de Google OAuth2

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/ARU9685/circulo-verde.git
cd circulo-verde

# 2. Crear el archivo de variables de entorno
cp .env.example .env
# Editar .env con tus claves

# 3. Arrancar todos los servicios
docker-compose up -d --build
```

### Servicios disponibles

| Servicio | URL |
|----------|-----|
| Aplicación | http://localhost:8080 |
| phpMyAdmin | http://localhost:8081 |

### Variables de entorno necesarias

```
DB_ROOT_PASSWORD=
DB_USER=
DB_PASSWORD=
WEATHER_API_KEY=
AEMET_API_KEY=
SATIVUM_TOKEN=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```

---

## 🗄️ Estructura de la base de datos

| Tabla | Descripción |
|-------|-------------|
| `usuario` | Usuarios registrados con zona climática |
| `tarea` | Tareas del calendario por usuario |
| `producto_inventario` | Inventario del huerto |
| `entrada_diario` | Entradas del diario personal |

Las tablas se crean automáticamente al arrancar la aplicación (`ddl-auto=update`).

---

## 📁 Estructura del proyecto

```
src/
├── main/
│   ├── java/com/circuloverde/circulo_verde/
│   │   ├── config/          # Seguridad (Spring Security, BCrypt)
│   │   ├── controller/      # Controladores web
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositorios Spring Data
│   │   └── service/         # Lógica de negocio + APIs externas
│   └── resources/
│       ├── templates/       # Vistas Thymeleaf
│       ├── static/          # CSS, JS, imágenes
│       └── application.properties
└── test/                    # Tests con Mockito
```

---

## 👩‍💻 Autora

**Amparo** — Proyecto Final de Ciclo Formativo de Desarrollo de Aplicaciones Web

---

## 📄 Licencia

Este proyecto ha sido desarrollado con fines educativos.



