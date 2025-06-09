package org.example.config;

import org.example.model.UtbAuthority;
import org.example.model.UtbPermission;
import org.example.model.UtbRole;
import org.example.security.CustomUserDetailsService;
import org.example.security.securityFilter.CustomUsernamePasswordAuthenticationFilter;
import org.example.security.securityFilter.JWTTokenGenerationFilter;
import org.example.security.securityFilter.JWTTokenValidationFilter;
import org.example.service.PermissionServices;
import org.example.service.UserServices;
import org.example.utility.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security configuration class for defining security settings and filters.
 *
 * This configuration class sets up various security aspects of the application including session management,
 * CORS, CSRF, exception handling, security filters, authorization rules, and authentication type.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServices userServices;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PermissionServices permissionServices;

    @Autowired
    private Environment environment;

    @Autowired
    private JWTTokenValidationFilter jwtTokenValidationFilter;

    @Autowired
    private JWTTokenGenerationFilter jwtTokenGenerationFilter;

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
        configureSessionManagement(http);
        configureCORS(http);
        configureCSRF(http);
        configureExceptionHandling(http);
        configureFilters(http);
        configureAuthorization(http);
        configurePermitAllPermissions(http);
        configureAuthenticationType(http);
        return http.build();
    }

    /**
     * Provides a custom {@link AuthenticationEntryPoint} to handle unauthorized access attempts.
     *
     * This entry point handles authentication errors by sending a custom error response with a status of
     * {@link HttpStatus#UNAUTHORIZED}. It utilizes {@link ResponseUtil} to format the error response.
     *
     * @return a custom {@link AuthenticationEntryPoint} instance
     */
    private AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
                ResponseUtil.setErrorResponse(HttpStatus.UNAUTHORIZED, ex, "Unauthorized", response);
            }
        };
    }

    /**
     * Provides a custom {@link AccessDeniedHandler} to handle access denied errors.
     *
     * This handler sends a custom error response when a user tries to access a resource they are not
     * authorized to access. It uses {@link ResponseUtil} to format the response with a status of
     * {@link HttpStatus#FORBIDDEN}.
     *
     * @return a custom {@link AccessDeniedHandler} instance
     */
    private AccessDeniedHandler customAccessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
                ResponseUtil.setErrorResponse(HttpStatus.FORBIDDEN, ex, "Access Denied", response);
            }
        };
    }

    /**
     * Creates a {@link CustomUsernamePasswordAuthenticationFilter} for handling username and password authentication.
     *
     * This filter processes login requests and authenticates users based on provided credentials. It uses a
     * {@link ProviderManager} configured with the {@link AuthenticationProvider} to handle authentication.
     * The login URL is retrieved from environment properties.
     *
     * @return a {@link CustomUsernamePasswordAuthenticationFilter} instance
     */
    private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        CustomUsernamePasswordAuthenticationFilter authFilter = new CustomUsernamePasswordAuthenticationFilter(providerManager);
        authFilter.setFilterProcessesUrl(environment.getProperty("login.url"));
        return authFilter;
    }

    /**
     * Configures session management to be stateless.
     *
     * This configuration ensures that Spring Security does not create or use HTTP sessions, as the application
     * uses JWT tokens for authentication and session management. This is achieved by setting the session creation
     * policy to {@link SessionCreationPolicy#STATELESS}.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
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
     * Configures custom exception handling for authentication and access denied errors.
     *
     * This method sets up custom handlers for authentication errors and access denied scenarios. The custom
     * handlers provide user-friendly error messages and HTTP status codes. The customAuthenticationEntryPoint
     * and customAccessDeniedHandler methods are used to create these handlers.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler()));
    }

    /**
     * Configures security filters for JWT token validation and generation.
     *
     * This method adds custom filters to the security filter chain. The {@link JWTTokenValidationFilter} is
     * added before the {@link BasicAuthenticationFilter} to validate JWT tokens in incoming requests. The
     * {@link JWTTokenGenerationFilter} is added after the {@link BasicAuthenticationFilter} to generate JWT
     * tokens upon successful authentication. This ensures that tokens are validated before accessing secured
     * resources and generated after successful login.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureFilters(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtTokenValidationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(jwtTokenGenerationFilter, BasicAuthenticationFilter.class);
    }

    /**
     * Configures default permissions to permit all requests.
     *
     * This method sets up a default rule that permits all requests to the application without any authorization
     * checks. This configuration is useful for endpoints that need to be accessible to everyone, such as public
     * APIs or static resources.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configurePermitAllPermissions(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll();
    }

    /**
     * Configures authorization rules based on permissions and authorities.
     *
     * This method sets up authorization rules for various URL patterns based on permissions defined in the
     * {@link PermissionServices}. It retrieves permissions and their associated authorities and roles, and then maps
     * each permission to a set of authorities required to access specific URL patterns. This ensures that only
     * users with the appropriate authorities or role can access restricted resources.
     *
     * @param http the {@link HttpSecurity} object to configure
     * @throws Exception if an error occurs during configuration
     */
    private void configureAuthorization(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        Map<String, Set<String>> permissionRolesMap = new HashMap<>();
        Map<String, Set<String>> permissionAuthoritiesMap = new HashMap<>();
        for (UtbPermission permission : permissionServices.getPermissions(environment.getProperty("spring.application.name"))) {
            Set<UtbRole> roles = permission.getRoles();
            Set<UtbAuthority> authorities = permission.getAuthorities();
            if (roles != null) {
                Set<String> roleNames = roles.stream()
                        .map(UtbRole::getName)
                        .collect(Collectors.toSet());
                permissionRolesMap.put(permission.getName(), roleNames);
            }
            if (authorities != null) {
                Set<String> authorityNames = authorities.stream()
                        .map(UtbAuthority::getName)
                        .collect(Collectors.toSet());
                permissionAuthoritiesMap.put(permission.getName(), authorityNames);
            }
        }
        for (Map.Entry<String, Set<String>> permissionEntry : permissionRolesMap.entrySet()) {
            String[] roles = permissionEntry.getValue().toArray(new String[0]);
            if(roles.length>0) registry.requestMatchers(permissionEntry.getKey()).hasAnyRole(roles);
        }
        for (Map.Entry<String, Set<String>> permissionEntry : permissionAuthoritiesMap.entrySet()) {
            String[] authorities = permissionEntry.getValue().toArray(new String[0]);
            if(authorities.length>0) registry.requestMatchers(permissionEntry.getKey()).hasAnyAuthority(authorities);
        }
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
