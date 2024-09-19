package org.example.config;

import feign.Logger;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for defining beans used in the application.
 * This class is responsible for setting up beans related to HTTP communication,
 * including RestTemplate and Feign logging configurations.
 */
@Configuration  // Indicates that this class contains Spring configuration and bean definitions.
public class BeanConfig {

    /**
     * Defines a RestTemplate bean with load-balancing capabilities.
     * This RestTemplate is used for making HTTP requests and is annotated with
     * @LoadBalanced to enable client-side load balancing for service calls in a
     * Spring Cloud environment.
     *
     * @return A RestTemplate instance configured with load-balancing support.
     */
    @Bean
    @LoadBalanced  // Indicates that this RestTemplate bean will use Ribbon for client-side load balancing.
    public RestTemplate getRestTemplate() {
        return new RestTemplate();  // Returns a new instance of RestTemplate.
    }

    /**
     * Defines a Feign Logger.Level bean for logging Feign client interactions.
     * This configuration sets the logging level to FULL, which provides detailed
     * logs of the requests and responses, including headers, body, and metadata.
     *
     * @return The logging level for Feign clients, set to FULL for comprehensive logging.
     */
    @Bean
    public Logger.Level getFeignLoggerLevel() {
        return Logger.Level.FULL;  // Configures Feign clients to log full request and response details.
    }
}
