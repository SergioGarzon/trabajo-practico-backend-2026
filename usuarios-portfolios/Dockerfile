# Paso 1: Usar una imagen base oficial de Java 17 para correr la app
FROM eclipse-temurin:17-jre-alpine

# Definir el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar generado desde tu target local al contenedor
# Nota: Si el nombre de tu jar cambia, podés usar el comodín *.jar
COPY target/*.jar app.jar

# Exponer el puerto en el que escucha tu aplicación de Spring
EXPOSE 8081

# Ejecutar la aplicación usando el perfil de docker que creaste
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]