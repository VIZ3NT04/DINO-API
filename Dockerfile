FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 1. Instala Maven y verifica encoding del sistema
RUN apt-get update && \
    apt-get install -y maven && \
    echo "LANG=C.UTF-8" > /etc/default/locale

# 2. Copia solo lo necesario
COPY pom.xml .
COPY src ./src

# 3. Fuerza encoding UTF-8 durante el build
RUN mvn clean package -DskipTests -Dfile.encoding=UTF-8

# 4. Copia el JAR
COPY target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]