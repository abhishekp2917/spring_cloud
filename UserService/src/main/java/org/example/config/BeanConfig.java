package org.example.config;

import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class to define beans for the application context.
 * <p>
 * This class is marked with {@link Configuration} to indicate that it contains
 * bean definitions for the Spring context. It primarily configures a custom password encoder bean
 * using BCrypt, which is widely used for securely hashing passwords in a web application.
 * </p>
 */
@Configuration
public class BeanConfig {

    /**
     * Creates a BCrypt-based {@link PasswordEncoder} bean.
     * <p>
     * This bean will be managed by the Spring context and can be injected
     * into services or components where password encoding is required.
     * The BCrypt algorithm is used to provide a strong password hash that includes salting
     * and iterative hashing to protect against brute force attacks.
     * </p>
     *
     * @return a {@link BCryptPasswordEncoder} instance to encode passwords securely.
     */
    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an {@link HttpExchangeRepository} bean to store HTTP exchange data in memory.
     * <p>
     * This bean provides the mechanism for storing the last 100 HTTP request-response exchanges
     * that pass through the Spring Boot application. The {@link InMemoryHttpExchangeRepository}
     * keeps these exchanges in an in-memory structure, making them available for Actuator's
     * {@code httpexchanges} endpoint. It is useful for inspecting and monitoring HTTP traffic
     * in the application.
     * </p>
     *
     * @return an {@link InMemoryHttpExchangeRepository} instance to store HTTP exchanges.
     */
    @Bean
    public HttpExchangeRepository getHttpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
