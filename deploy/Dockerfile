FROM openjdk:17-slim

WORKDIR /app
COPY m1l1-first-app/build/libs/m1l1-first-app-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "app.jar"]