# Usar imagen base oficial de Java 17
FROM eclipse-temurin:17-jre-alpine

# Definir directorio de trabajo
WORKDIR /app

# Copiar el archivo .jar compilado
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 8089

# Ejecutar la aplicación
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
