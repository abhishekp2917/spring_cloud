package org.example.security.securityFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ServerUtil;
import org.example.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JWTTokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private Environment environment;

    /**
     * Validates the JWT token for each non-public request and sets the authentication in the security context.
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
        // Extract the JWT token from the request cookie
        String jwt = ServerUtil.extractCookieFromHttpRequest(request, environment.getProperty("jwt.cookie.name"));
        if (jwt != null) {
            try {
                // Retrieve the secret key and validate the JWT token against the key
                SecretKey key = Keys.hmacShaKeyFor(environment.getProperty("jwt.secretKey").getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
                // If the JWT token is valid, extract the username and associated authorities
                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");
                // Create an Authentication token and set it in the security context
                // This indicates to Spring Security that the user has been authenticated,
                // so the BasicAuthenticationFilter will not be executed
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch (Exception ex) {
                // throwing InvalidTokenException pf type AuthenticationException so that it will trigger AuthenticationEntryPoint
                throw new InvalidTokenException(ex.getMessage());
            }
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Determines if the filter should be applied to the current request.
     * The filter is not applied to the login URL to avoid interference with the first-time login request.
     *
     * @param request The HTTP request
     * @return True if the filter should be skipped, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().equals(environment.getProperty("login.url"));
    }
}
