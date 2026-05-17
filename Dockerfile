# ── Etapa 1: compilar con Maven ───────────────────────────────────────────────
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiar primero solo el pom.xml para cachear dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copiar el código fuente y compilar
COPY src ./src
RUN ./mvnw clean package -DskipTests -q

# ── Etapa 2: imagen final ligera solo con JRE ─────────────────────────────────
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar solo el JAR de la etapa anterior
COPY --from=build /app/target/circulo-verde-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]
