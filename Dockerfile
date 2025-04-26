#Usa la imagen oficial de OpenJDK 17 (versión slim para reducir tamaño)
FROM eclipse-temurin:17-jdk-jammy

#Directorio de trabajo dentro del contenedor
WORKDIR /app

#Copia el JAR de tu aplicación (ajusta el nombre según tu build)
COPY target/*.jar app.jar

#Puerto expuesto (ajusta al puerto que usa tu Spring Boot, normalmente 8080)
EXPOSE 8080

#Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]