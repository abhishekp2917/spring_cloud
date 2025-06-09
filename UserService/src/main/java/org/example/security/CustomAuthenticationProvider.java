package org.example.security;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.authentication.AuthenticationProvider;
        import org.springframework.security.authentication.BadCredentialsException;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.core.AuthenticationException;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.security.crypto.password.PasswordEncoder;
        import org.springframework.stereotype.Component;

/**
 * CustomAuthenticationProvider implements Spring Security's AuthenticationProvider interface to provide a custom
 * authentication mechanism.
 *
 * This class is responsible for authenticating users based on the username and password provided.
 * It integrates with Spring Security's authentication system and is defined as a Spring component
 * so that it can be managed by the Spring container and injected wherever needed.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticate method is used to validate the provided username and password.
     *
     * @param authentication An Authentication object containing the credentials (username and password) to be authenticated.
     * @return An Authentication object if the credentials are valid, otherwise returns null.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Extracting username from the Authentication object
        String username = authentication.getName();
        // Extracting password from the Authentication object
        String password = authentication.getCredentials().toString();

        // Loading user details by username from the custom user details service
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Checking if the user exists
        if (userDetails != null) {
            // Verifying the provided password against the stored password using password encoder
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                // If authentication is successful, return an Authentication object with user details and authorities
                return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
            } else {
                // Throw an exception if the password does not match
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            // Throw an exception if the user is not found
            throw new BadCredentialsException("Invalid username");
        }
    }

    /**
     * Supports method is used to determine if this AuthenticationProvider can handle the provided Authentication object.
     * Whenever AuthenticationManager gets an Authentication token, it will iterate over all the configured AuthenticationProvider
     * and will call this method to check if any particular AuthProvider supports the provided Auth Token.
     *
     * @param authentication The Authentication class to be checked.
     * @return True if this provider can handle the provided Authentication object, otherwise false.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // This AuthenticationProvider supports UsernamePasswordAuthenticationToken class
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

