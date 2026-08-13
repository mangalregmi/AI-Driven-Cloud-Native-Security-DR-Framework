FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/unified-orchestration-engine-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
