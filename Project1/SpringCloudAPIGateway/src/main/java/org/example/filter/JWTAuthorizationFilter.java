package org.example.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.JWTUtil;
import org.example.ServerUtil;
import org.example.dto.ErrorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JWTAuthorizationFilter is a custom filter used in the API Gateway to validate JWT tokens in incoming requests.
 * It extends AbstractGatewayFilterFactory and checks if the JWT token is present in the request cookies.
 * If the token is missing or invalid, the filter responds with an HTTP 401 Unauthorized status and a JSON error message.
 *
 * This filter needs to be configured explicitly to the routes and will be triggered only for those routes which has this filter configured.
 */
@Component
public class JWTAuthorizationFilter extends AbstractGatewayFilterFactory<JWTAuthorizationFilter.Config> {

    @Autowired
    private Environment environment;

    /**
     * Apply method to define the filter logic.
     * This method retrieves the JWT token from the request cookies, validates it, and returns an error response if the token is missing or invalid.
     *
     * @param config The configuration object for the filter.
     * @return A GatewayFilter instance.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (serverWebExchange, gatewayFilterChain) -> {
            ServerHttpRequest request = serverWebExchange.getRequest();
            // Extract the JWT token from the request cookies using the cookie name defined in the environment properties.
            String JWTToken = ServerUtil.extractCookieFromHttpRequest(request, environment.getProperty("jwt.cookie.name"));

            // Validate the JWT token. If the token is missing or invalid, return an HTTP 401 Unauthorized response with a JSON error message.
            if (JWTToken == null || !JWTUtil.validateJWTToken(JWTToken, environment.getProperty("jwt.secretKey"))) {
                // Create an error response DTO with the current timestamp, status code, error, and message.
                ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .error("Unauthorized")
                        .message("JWT token is missing or is invalid")
                        .build();

                // Get the response object and set the status code to 401 Unauthorized.
                ServerHttpResponse response = serverWebExchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                // Set the Content-Type header to application/json to indicate the response body is in JSON format.
                response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");

                try {
                    // Serialize the error response DTO to a JSON byte array.
                    byte[] bytes = new ObjectMapper().writeValueAsBytes(errorResponse);
                    // Wrap the byte array in a DataBuffer and write it to the response.
                    DataBuffer buffer = response.bufferFactory().wrap(bytes);
                    return response.writeWith(Flux.just(buffer));
                } catch (Exception ex) {
                    // If an exception occurs during serialization, complete the response without writing any body.
                    return response.setComplete();
                }
            }
            // If the JWT token is valid, continue with the filter chain.
            return gatewayFilterChain.filter(serverWebExchange);
        };
    }

    /**
     * Configuration class for the JWTAuthorizationFilter.
     * This class can be used to pass configuration properties to the filter.
     */
    public static class Config {
        // Add configuration properties if needed.
    }
}
