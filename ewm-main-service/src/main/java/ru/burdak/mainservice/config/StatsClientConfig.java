package ru.burdak.mainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.burdak.ewmstatsclient.client.StatsClient;

@Configuration
public class StatsClientConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public StatsClient statsClient(
        RestTemplate restTemplate,
        @Value("${stats-server.url}") String statsServerUrl
    ) {
        return new StatsClient(restTemplate, statsServerUrl);
    }
}
