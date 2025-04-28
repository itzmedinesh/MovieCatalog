FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/MovieCatalog-0.0.1-SNAPSHOT.jar MovieCatalog.jar
EXPOSE 8080
CMD ["java","-jar","MovieCatalog.jar"]
