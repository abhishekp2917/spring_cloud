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
 * ResponseLoggingPostFilter is a global post-filter that logs outgoing responses in the API Gateway.
 * It implements the GlobalFilter and Ordered interfaces.
 *
 * This filter is triggered after the request has been processed by the downstream service and a response is generated.
 * The order of this filter is set to 0, meaning it has the lowest precedence.
 *
 * The filter logs all response headers to the console.
 */
@Component
public class ResponseLoggingPostFilter implements GlobalFilter, Ordered {

    /**
     * This method is called for each outgoing response. It logs the response headers.
     *
     * @param exchange The current server exchange.
     * @param chain Provides a way to delegate to the next filter.
     * @return A Mono<Void> that indicates when response processing is complete.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            HttpHeaders headers = exchange.getResponse().getHeaders();
            Set<String> headerNames = headers.keySet();
            System.out.println("Response headers :");
            headerNames.forEach((headerName) -> {
                String headerValue = headers.getFirst(headerName);
                System.out.println(String.format("%s : %s", headerName, headerValue));
            });
        }));
    }

    /**
     * Specifies the order of this filter. The lower the value, the lower the precedence.
     * Meaning this filter will be executed after all the global filters being executed.
     *
     * @return The order value. This filter is set to have the lowest precedence with an order of 0.
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
