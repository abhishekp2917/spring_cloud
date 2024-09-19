package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import java.util.Collections;

/**
 * Spring Security configuration class for defining security settings and filters.
 *
 * This configuration class sets up various security aspects of the application including session management,
 * CORS, CSRF, exception handling, security filters, authorization rules, and authentication type.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the {@link SecurityFilterChain} with various security settings.
     *
     * This method sets up the security filter chain by configuring session management, CORS,
     * CSRF, exception handling, filters, authorization rules, and authentication type.
     *
     * @param http the {@link HttpSecurity} object used for configuration
     * @return a configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureCORS(http);
        configureCSRF(http);
        configurePermissions(http);
        configureAuthenticationType(http);
        return http.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings.
     *
     * This method sets up CORS to allow requests from specified origins, headers, and methods. It defines a
     * {@link CorsConfiguration} object with allowed origins, credentials, headers, and methods to be included
     * in the response headers of preflight requests.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureCORS(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource((request) -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:8010"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            return corsConfiguration;
        }));
    }

    /**
     * Disables CSRF (Cross-Site Request Forgery) protection.
     *
     * CSRF protection is disabled because the application uses JWT tokens for authentication, which are not
     * susceptible to CSRF attacks. This configuration ensures that CSRF tokens are not required in requests.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureCSRF(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
    }

    /**
     * Configures default permissions to authenticate requests.
     *
     * This method sets up a default rule that authenticate all requests to the application without any authorization
     * checks.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configurePermissions(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    /**
     * Configures the authentication type to use HTTP Basic authentication.
     *
     * This method enables HTTP Basic authentication, which requires users to provide their credentials (username
     * and password) in the request headers for authentication. This setup is typically used in conjunction with
     * other authentication mechanisms like JWT tokens.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureAuthenticationType(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
    }
}
