package io.demo.MovieCatalog;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.sql.DataSource;

@SpringBootApplication
public class MovieCatalogApplication {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(MovieCatalogApplication.class, args);
    }

    @Bean
    public DataSource dataSource(@Value("${secrets.manager.region}") String awsRegion) throws Exception {
        // Fetch secrets from AWS Secrets Manager
        GetSecretValueResponse response;
        try (SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(Region.of(awsRegion))
                .build()) {

            response = secretsClient.getSecretValue(
                    GetSecretValueRequest.builder()
                            .secretId("movie-search/movie-catalog-db")
                            .build());
        }

        JsonNode secretJson = objectMapper.readTree(response.secretString());

        String url = String.format("jdbc:%s://%s:%s/%s", secretJson.get("engine").asText(), secretJson.get("host").asText(), secretJson.get("port").asText(), secretJson.get("dbname").asText());

        return DataSourceBuilder.create()
                .url(url)
                .username(secretJson.get("username").asText())
                .password(secretJson.get("password").asText())
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

}
