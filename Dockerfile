FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 1. Copia solo los archivos necesarios para construir
COPY pom.xml .
COPY src ./src

# 2. Construye el proyecto (genera el JAR)
RUN ./mvnw clean package -DskipTests

# 3. Copia SOLO el JAR resultante (optimiza tamaño de imagen)
COPY target/dino-api-*.jar app.jar

# 4. Puerto y ejecución
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]  # ¡Usa app.jar que copiamos antes!