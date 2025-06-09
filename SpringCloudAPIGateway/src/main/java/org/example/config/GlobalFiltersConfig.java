package org.example.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * Configuration class for defining global filters in Spring Cloud Gateway.
 *
 * Global filters can be used to perform operations on the request and response
 * at a global level, affecting all routes in the gateway. Filters are executed
 * in the order specified by the @Order annotation.
 */
@Configuration
public class GlobalFiltersConfig {

    /**
     * Defines the first global pre-filter with the highest precedence.
     * This filter will be executed first among all global pre-filters.
     *
     * The primary purpose of the @Order annotation is to control the order in which
     * beans are processed when multiple beans of the same type are present.
     * This is useful in scenarios where the order of execution affects the application behavior,
     * such as in filters, interceptors, or event listeners.
     *
     * @return a GlobalFilter instance that logs a message before and after
     *         processing the request.
     */
    @Order(1)
    @Bean
    public GlobalFilter firstPreFilter() {
        return (exchange, chain) -> {
            // Log message indicating the execution of the first global pre-filter
            System.out.println("First global pre-filter executed...");
            // Process the request and then execute post-filter logic
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Log message indicating the execution of the first global post-filter
                System.out.println("Third global post-filter executed...");
            }));
        };
    }

    /**
     * Defines the second global pre-filter with medium precedence.
     * This filter will be executed after the first pre-filter but before
     * the third pre-filter.
     *
     * @return a GlobalFilter instance that logs a message before and after
     *         processing the request.
     */
    @Order(2)
    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            // Log message indicating the execution of the second global pre-filter
            System.out.println("Second global pre-filter executed...");
            // Process the request and then execute post-filter logic
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Log message indicating the execution of the second global post-filter
                System.out.println("Second global post-filter executed...");
            }));
        };
    }

    /**
     * Defines the third global pre-filter with the lowest precedence.
     * This filter will be executed last among all global pre-filters.
     *
     * @return a GlobalFilter instance that logs a message before and after
     *         processing the request.
     */
    @Order(3)
    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            // Log message indicating the execution of the third global pre-filter
            System.out.println("Third global pre-filter executed...");
            // Process the request and then execute post-filter logic
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Log message indicating the execution of the third global post-filter
                System.out.println("First global post-filter executed...");
            }));
        };
    }
}
