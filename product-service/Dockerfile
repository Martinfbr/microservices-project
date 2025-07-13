# Imagen base de Java 17 con Alpine
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el JAR generado por Gradle
COPY build/libs/product-service-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]