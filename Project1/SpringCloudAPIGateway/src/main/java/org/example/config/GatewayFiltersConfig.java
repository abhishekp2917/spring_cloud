package org.example.config;

import org.example.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining custom route locators and filters for Spring Cloud Gateway.
 * This class sets up routing rules and various filters to manipulate requests and responses.
 */
@Configuration
public class GatewayFiltersConfig {

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    /**
     * Defines a custom RouteLocator bean.
     * This bean sets up routing rules for the API Gateway to forward requests to the appropriate microservices.
     * It also includes various filters to manipulate the request and response headers, parameters, and paths.
     *
     * @param builder the RouteLocatorBuilder to build the routes
     * @return a configured RouteLocator instance
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Define a route for /status and /env path in user-ms to apply custom filters to these paths
                // Since these paths are secured, JWT token must be present in the request to these paths.
                // To check if token is present, JwtAuthorizationFilter custom filer is being applied to this path which will intercept the request before passing the request to user-ms
                .route("users-authenticated-route", r -> r
                        .path("/user/status", "/user/env")
                        .filters(f -> f.filter(jwtAuthorizationFilter.apply(new JWTAuthorizationFilter.Config())))
                        .uri("lb://user-ms"))
                // Define a route for user-related requests
                // This route matches any request with the path starting with /user/ and request type should be 'GET' or 'POST'
                // There should be a Cookie in the header and a query parameter named 'version' in the request URL
                .route("users-route", r -> r
                        .path("/user/**")
//                        .and()
//                        .method("GET", "POST").and()
//                        .header("Cookie", "*").and()
//                        .query("version", "1.0")
                        // Define filters to manipulate the request and response for this route
                        // These filters are used to manipulate the request before routing the request to respective microservice
                        // and also to manipulate response before sending it to the client
                        .filters(f -> {
                            // Adds a custom request header named "scope" with the value "test"
                            f.addRequestHeader("scope", "test");

                            // Adds a query parameter named "userId" with the value "1" to the request
                            f.addRequestParameter("userId", "1");

                            // Adds a custom response header named "status" with the value "Active"
                            f.addResponseHeader("status", "Active");

                            // Rewrites the request path from /example/<segment> to /newExample/<segment>
                            f.rewritePath("/example/(?<segment>.*)", "/newExample/${segment}");

                            // Rewrites the response header "status" from "Active" to "Alive"
                            f.rewriteResponseHeader("status", "Active", "Alive");

                            // Placeholder for removing a request header (currently does nothing)
                            f.removeRequestHeader("");

                            // Placeholder for removing a response header (currently does nothing)
                            f.removeResponseHeader("");

                            // Sets the request header "Cache-Control" to "no-cache", replacing any existing value
                            f.setRequestHeader("Cache-Control", "no-cache");

                            // Sets the response header "Content-Type" to "application/json", replacing any existing value
                            f.setResponseHeader("Content-Type", "application/json");

                            // Strips the first segment of the request path, effectively removing the leading segment
                            f.stripPrefix(1);

                            // Adds "/user" as a prefix to the request path
                            f.prefixPath("/user");

                            return f;
                        })
                        // Routes this request to the "user-ms" microservice using load balancing
                        .uri("lb://user-ms"))
                // Define a route for account management-related requests
                // This route matches any request with the path starting with /account/
                .route("account-management-route", r -> r
                        .path("/account/**")
                        // Routes this request to the "accountManagement-ms" microservice using load balancing
                        .uri("lb://accountManagement-ms"))
                // Define a route for product related requests
                // This route matches any request with the path starting with /product/
                .route("product-route", r -> r
                        .path("/product/**")
                        .uri("lb://product-ms"))
                // Define a route for order related requests
                // This route matches any request with the path starting with /order/
                .route("order-route", r -> r
                        .path("/order/**")
                        .uri("lb://order-ms"))
                // Define a route for the user-ms actuators
                // This route matches any request with the path starting with /monitor/
                .route("user-ms-actuator-route", r -> r
                        .path("/monitor/**")
                        .and()
                        .method("GET", "POST")
                        .uri("lb://user-ms"))
                .build();
    }
}

