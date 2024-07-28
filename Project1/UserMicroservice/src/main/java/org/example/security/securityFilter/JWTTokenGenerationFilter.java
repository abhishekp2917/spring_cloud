package org.example.security.securityFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class JWTTokenGenerationFilter extends OncePerRequestFilter {

    @Autowired
    private Environment environment;

    /**
     * Generates a JWT token and saves it in a response cookie if the user is authenticated.
     *
     * @param request     The HTTP request
     * @param response    The HTTP response
     * @param filterChain The filter chain
     * @throws ServletException If an error occurs during filtering
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the authentication token from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Retrieve the secret key from the environment properties
            SecretKey key = Keys.hmacShaKeyFor(environment.getProperty("jwt.secretKey").getBytes(StandardCharsets.UTF_8));

            // Build the JWT token including required details in the payload and sign it with the secret key
            String jwt = Jwts.builder()
                    .issuer("Abhishek")
                    .subject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + 30000000)) // Token validity set to 30,000,000 ms (approximately 8 hours)
                    .signWith(key)
                    .compact();

            // Place the generated token inside a response cookie
            Cookie JWTCookie = new Cookie(environment.getProperty("jwt.cookie.name"), jwt);
            // Ensure cookie is accessible for all the paths
            JWTCookie.setPath("/");
            // Ensure cookie is not accessible via JavaScript
            JWTCookie.setHttpOnly(true);
            response.addCookie(JWTCookie);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Determines if the filter should be applied to the current request.
     * The filter is applied only if the requested URL is '/user/login', as this is used for the initial authentication.
     *
     * @param request The HTTP request
     * @return True if the filter should be applied, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals(environment.getProperty("login.url"));
    }

    /**
     * Converts a collection of GrantedAuthority objects into a comma-separated string.
     *
     * @param collection The collection of GrantedAuthority
     * @return A comma-separated string of authority names
     */
    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
