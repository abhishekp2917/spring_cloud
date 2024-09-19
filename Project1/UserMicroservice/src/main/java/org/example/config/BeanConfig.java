package org.example.config;

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
}
