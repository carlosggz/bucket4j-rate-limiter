FROM eclipse-temurin:21-jdk-alpine
COPY build/libs/bucket4j-rate-limiter-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker","/app.jar"]