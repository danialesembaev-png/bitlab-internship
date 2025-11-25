FROM eclipse-temurin:21-jdk



COPY build/libs/to-do-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "app.jar"]

