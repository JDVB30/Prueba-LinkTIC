# --- Stage 1: Build (Compilación) ---
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copiamos el pom y descargamos dependencias (optimiza caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código y generamos el JAR
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime (Ejecución) ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos solo el archivo JAR resultante del stage anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto (cambia a 8082 en el otro microservicio)
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]