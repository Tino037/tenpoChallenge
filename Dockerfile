# Etapa de construcción
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar dependencias necesarias
RUN apk add --no-cache curl dos2unix

# Establecer el directorio de trabajo
WORKDIR /workspace/app

# Copiar primero solo los archivos necesarios para Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Convertir los finales de línea y dar permisos
RUN dos2unix mvnw && chmod +x mvnw

# Descargar dependencias
RUN ./mvnw dependency:go-offline -B

# Copiar el resto del proyecto
COPY src src

# Compilar y empaquetar la aplicación
RUN ./mvnw clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jre-alpine

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /workspace/app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"] 