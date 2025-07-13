# Usa una imagen base oficial de Java + Gradle o Maven
FROM eclipse-temurin:17-jdk

# Crea directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el build de tu app (suponiendo que ya has generado el JAR con ./gradlew bootJar)
COPY build/libs/product-service-*.jar app.jar

# Expone el puerto 8081 dentro del contenedor
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]