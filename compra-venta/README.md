# Microservicio de Compra-Venta

Este microservicio actúa como el motor de emparejamiento central del ecosistema. Su responsabilidad es procesar y emparejar las órdenes de compra y venta de los distintos usuarios y portfolios para concretar las transacciones.

## Red y Puertos
- **Puerto por defecto**: `8082`

## Instrucciones de Compilación
Para compilar el proyecto omitiendo los tests, ejecuta:
```bash
mvn clean package -DskipTests
```

## Tecnologías Utilizadas
- Java 17
- Spring Boot
- MySQL
- Keycloak
- Feign
