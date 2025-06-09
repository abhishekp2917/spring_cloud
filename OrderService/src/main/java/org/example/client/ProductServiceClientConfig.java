package org.example.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class for Feign Client specific to the ProductService.
 * This class defines an interceptor that modifies Feign requests to include
 * specific headers, such as the JWT Token retrieved from cookies in incoming HTTP requests.
 */
@Configuration
public class ProductServiceClientConfig {

    // Injecting the Environment object to fetch application properties, like the cookie name
    @Autowired
    private Environment environment;

    /**
     * Bean definition for Feign RequestInterceptor.
     * This interceptor is used to modify outgoing Feign client requests.
     * Specifically, it adds the JWT Token (stored as a cookie in the incoming HTTP request)
     * to the outgoing request as a 'Cookie' header.
     *
     * @return A configured RequestInterceptor that adds the JWT Token to requests.
     */
    @Bean
    public RequestInterceptor productServiceRequestInterceptor() {
        return new RequestInterceptor() {

            /**
             * Intercepts every outgoing Feign Client request and applies necessary headers.
             * In this case, it retrieves the JWT Token from the incoming request's cookies and adds it
             * to the outgoing request as a 'Cookie' header.
             *
             * @param requestTemplate The request template of the outgoing Feign request where headers can be modified.
             */
            @Override
            public void apply(RequestTemplate requestTemplate) {
                // Obtain the current HttpServletRequest using RequestContextHolder
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (requestAttributes != null) {
                    HttpServletRequest httpServletRequest = requestAttributes.getRequest();

                    // Get the cookies from the incoming HttpServletRequest
                    Cookie[] cookies = httpServletRequest.getCookies();

                    // Get the configured cookie name for JWT Token from the application properties
                    String jwtCookieName = environment.getProperty("jwt.cookie.name");

                    // If cookies are present in the request
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            // Check if the cookie matches the JWT Token cookie name
                            if (jwtCookieName.equals(cookie.getName())) {
                                // Add the JWT Token value as a 'Cookie' header in the outgoing Feign request
                                requestTemplate.header("Cookie", jwtCookieName + "=" + cookie.getValue());
                                break;  // Exit the loop once the JWT Token cookie is found
                            }
                        }
                    }
                }
            }
        };
    }
}
