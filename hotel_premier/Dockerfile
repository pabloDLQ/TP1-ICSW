# Etapa 1: Build con Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar aplicación (saltar tests para build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con JRE ligero
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR desde etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto de Spring Boot
EXPOSE 8080

# Variables de entorno para conexión a MySQL
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/hotel_premier?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
