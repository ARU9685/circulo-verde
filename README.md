# 🌱 CÍRCULO VERDE – Gestión de Huerto Personal

![Logo](circulo-verde/circulo-verde/src/main/resources/static/img/LogoCirculoVerde.jpeg)

Aplicación web desarrollada con **Spring Boot**, **Thymeleaf** y **MySQL**, diseñada para ayudar a los usuarios a gestionar su huerto personal habilitado a zonas climáticas: tareas, calendario, estadísticas y seguimiento diario.

---

##  Características principales

###  Gestión de usuarios
- Registro de nuevos usuarios  
- Inicio de sesión personalizado  
- Validación de datos (nombre, email, contraseña, ciudad, zona climática)  
- Fecha de registro automática  

###  Gestión del huerto
- Panel principal con acceso rápido a todas las funciones  
- Registro, edición y eliminación de tareas  
- Vista de calendario  
- Estadísticas básicas  
- Diario de actividades  

###   Base de datos
Motor: **MySQL / MariaDB**

Tablas principales:
- `usuario`
- `tareas`
- `calendario`
- `diario`
- `estadisticas`
- `zonasclimaticas`

---

##  Tecnologías utilizadas

| Tecnología | Uso |
|-----------|-----|
| **Java 17+** | Lógica de negocio |
| **Spring Boot** | Backend y controladores |
| **Spring Data JPA** | Acceso a datos |
| **Thymeleaf** | Plantillas HTML |
| **Bootstrap 5** | Estilos y diseño |
| **MySQL / MariaDB** | Persistencia |
| **Maven** | Gestión de dependencias |

---


