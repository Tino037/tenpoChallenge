# Etapa de construcción
FROM eclipse-temurin:21-jdk-alpine AS builder

# Instalar Maven y dependencias necesarias
RUN apk add --no-cache maven

# Establecer el directorio de trabajo
WORKDIR /workspace/app

# Copiar el archivo pom.xml
COPY pom.xml .

# Descargar dependencias
RUN mvn dependency:go-offline -B

# Copiar el resto del proyecto
COPY src src

# Compilar y empaquetar la aplicación
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jre-alpine

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /workspace/app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"] 