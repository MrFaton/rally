package ua.ponarin.rally.config;

import com.rallydev.rest.RallyRestApi;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RallyConfig {
    @Value("${rally.url}")
    private String rallyUrl;

    @Value("${rally.apiKey}")
    private String rallyApiKey;

    @Value("${rally.appName}")
    private String rallyAppName;

    @Bean
    @SneakyThrows
    public RallyRestApi createRallyRestApi() {
        var rallyRestApi = new RallyRestApi(new URI(rallyUrl), rallyApiKey);
        rallyRestApi.setApplicationName(rallyAppName);
        return  rallyRestApi;
    }
}
