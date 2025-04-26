FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 1. Instala Maven (solo necesario si no usas el wrapper)
RUN apt-get update && apt-get install -y maven

# 2. Copia los archivos del proyecto
COPY pom.xml .
COPY src ./src

# 3. Construye el proyecto (usa el comando directo de Maven)
RUN mvn clean package -DskipTests

# 4. Copia el JAR generado
COPY target/*.jar app.jar

# 5. Puerto y ejecución
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]