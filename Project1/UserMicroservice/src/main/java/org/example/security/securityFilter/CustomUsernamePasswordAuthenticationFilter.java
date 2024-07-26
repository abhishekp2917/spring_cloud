package org.example.security.securityFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.UtbUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Custom filter to handle username and password authentication requests.
 * This filter is used to process authentication requests and set the security context.
 */
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * Constructor to initialize the filter with the given AuthenticationManager.
     *
     * @param authenticationManager The AuthenticationManager to handle authentication requests.
     */
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * Processes the authentication request by extracting the username, password, and authorities from the request.
     * If authentication is successful, sets the security context with the authenticated user's details.
     *
     * @param request  The servlet request containing the authentication information.
     * @param response The servlet response.
     * @param chain    The filter chain.
     * @throws IOException      If an input or output error occurs.
     * @throws ServletException If a servlet error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // Parse the request input stream to extract user details
            UtbUser user = new ObjectMapper().readValue(request.getInputStream(), UtbUser.class);

            // Extract username and password from the user object
            String username = user.getUsername();
            if (username == null) username = "";
            String password = user.getPassword();
            if (password == null) password = "";

            // Convert user authorities to a list of GrantedAuthority
            List<GrantedAuthority> authorities = null;
            if (user.getAuthorities() != null) {
                authorities = user.getAuthorities()
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                        .collect(Collectors.toList());
            }

            // Create an authentication token with the username and password
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate the user with the AuthenticationManager
            Authentication authenticationResult = super.getAuthenticationManager().authenticate(authRequest);

            // If authentication is successful, set the authentication in the security context
            if (authenticationResult != null) {
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            // If an exception occurs, continue with the filter chain
            chain.doFilter(request, response);
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }
}
