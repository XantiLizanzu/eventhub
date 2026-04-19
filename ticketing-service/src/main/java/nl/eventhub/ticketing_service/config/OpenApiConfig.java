package nl.eventhub.ticketing_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Use an environment variable or hardcode the external URL
        String externalUrl = System.getenv().getOrDefault("TICKETING_BASE_URL", "http://localhost:8080/ticketing");

        return new OpenAPI()
                .servers(
                        List.of(
                                new Server().url(externalUrl)
                        )
                );
    }
}