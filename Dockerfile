FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/DINO-API.jar
ENTRYPOINT ["java", "-jar", "/app/DINO-API.jar"]
