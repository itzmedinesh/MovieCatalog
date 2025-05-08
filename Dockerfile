FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/MovieCatalog-0.0.1-SNAPSHOT.jar MovieCatalog.jar
EXPOSE 8080

# OpenTelemetry Java collector agent
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.15.0/opentelemetry-javaagent.jar /otel-javaagent.jar

# Configure opentelemetry collector agent
ENV JAVA_TOOL_OPTIONS="-javaagent:/otel-javaagent.jar"
ENV OTEL_SERVICE_NAME=MovieCatalog
ENV OTEL_EXPORTER_OTLP_ENDPOINT=https://my-observability-project-efe08b.ingest.us-east-1.aws.elastic.cloud:443
ENV OTEL_EXPORTER_OTLP_HEADERS=Authorization=ApiKey bmxwMHJwWUJ0SG11U293bHJwbUM6LU9UcmFDLUJYYWpNbHhMLXctU25wdw==
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_LOGS_EXPORTER=none
ENV OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
ENV OTEL_RESOURCE_ATTRIBUTES=service.version=1.0,deployment.environment=test

CMD ["java","-jar","MovieCatalog.jar"]
