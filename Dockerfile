FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/MovieCatalog-0.0.1-SNAPSHOT.jar MovieCatalog.jar
EXPOSE 8080

# OpenTelemetry Java collector agent
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.15.0/opentelemetry-javaagent.jar /otel-javaagent.jar

# Configure opentelemetry collector agent
ENV JAVA_TOOL_OPTIONS="-javaagent:/otel-javaagent.jar \
 -Dotel.service.name=MovieCatalog \
 -Dotel.exporter.otlp.endpoint=https://my-observability-project-efe08b.ingest.us-east-1.aws.elastic.cloud:443 \
 -Dotel.exporter.otlp.headers=Authorization=ApiKey bmxwMHJwWUJ0SG11U293bHJwbUM6LU9UcmFDLUJYYWpNbHhMLXctU25wdw== \
 -Dotel.exporter.oltp.protocol=http/protobuf \
 -Dotel.resource.attributes=service.version=1.0,deployment.environment=cloud \
 -Dmanagement.metrics.export.otlp.enabled=true \
 -Dmanagement.metrics.export.otlp.endpoint=https://my-observability-project-efe08b.ingest.us-east-1.aws.elastic.cloud \
 -Dmanagement.metrics.export.otlp.headers.Authorization=ApiKey bmxwMHJwWUJ0SG11U293bHJwbUM6LU9UcmFDLUJYYWpNbHhMLXctU25wdw=="

CMD ["java","-jar","MovieCatalog.jar"]