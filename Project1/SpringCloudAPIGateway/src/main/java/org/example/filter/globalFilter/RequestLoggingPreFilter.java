package org.example.filter.globalFilter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Set;

/**
 * RequestLoggingPreFilter is a global pre-filter that logs incoming requests in the API Gateway.
 * It implements the GlobalFilter and Ordered interfaces.
 *
 * This filter is triggered before the request is routed to the downstream service.
 * The order of this filter is set to 0, meaning it has the highest precedence.
 *
 * The filter logs the request path and all request headers to the console.
 *
 * Since this is global filter, it will be executed whenever a request will come to API gateway regardless of the request path
 */
@Component
public class RequestLoggingPreFilter implements GlobalFilter, Ordered {

    /**
     * This method is called for each incoming request. It logs the request path and headers.
     *
     * @param exchange The current server exchange.
     * @param chain Provides a way to delegate to the next filter.
     * @return A Mono<Void> that indicates when request processing is complete.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();
        System.out.println(String.format("Request path : %s", requestPath));
        HttpHeaders headers = exchange.getRequest().getHeaders();
        Set<String> headerNames = headers.keySet();
        System.out.println("Request headers :");
        headerNames.forEach((headerName) -> {
            String headerValue = headers.getFirst(headerName);
            System.out.println(String.format("%s : %s", headerName, headerValue));
        });
        // Continue the filter chain
        return chain.filter(exchange);
    }

    /**
     * Specifies the order of this filter. The lower the value, the higher the precedence.
     *
     * @return The order value. This filter is set to have the highest precedence with an order of 0.
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
