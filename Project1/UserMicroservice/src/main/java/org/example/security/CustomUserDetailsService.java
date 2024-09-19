package org.example.security;

import org.example.model.UtbAuthority;
import org.example.model.UtbRole;
import org.example.model.UtbUser;
import org.example.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomUserDetailsService implements Spring Security's UserDetailsService interface.
 *
 * providing implementation of UserDetailsService interface which is being used by AuthenticationProvider to fetch the
 * user details such as username, password etc from the storage system to compare with the user details
 * provided by user while logging in
 *
 * After implementing this we would be able to store the user details in DB or any other storage system and can use those
 * user details to log into application unlike earlier we have to pass 'user' as username and password as what was generated
 * when we first started the application
 *
 * This service is used to load user-specific data during the authentication process.
 * It is responsible for fetching user details from a storage system (e.g., database) and converting
 * these details into a format that Spring Security can use for authentication and authorization.
 *
 * By implementing this service, we can use database-stored user details for login, rather than relying
 * on hardcoded credentials.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // UserServices object is injected to interact with the User table in the database
    @Autowired
    private UserServices userServices;

    /**
     * Loads user details by username.
     *
     * This method is called by Spring Security during the authentication process.
     * It fetches the user's data from the database and returns it as a UserDetails object.
     *
     * @param username The username of the user to be fetched.
     * @return UserDetails containing the user's data including username, password, and authorities.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetching the UtbUser object from the database by username
        UtbUser user = userServices.findByUsername(username);

        // If the user is found, convert it to a UserDetails object
        if (user != null) {
            // If user exists, create User object which has username, password, role and authority details in it and return the object
            // SimpleGrantedAuthority being implementation of GrantedAuthority, we passed the list of GrantedAuthority to User object.
            // This list will have all the role and the authority that this particular user has
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (UtbRole role : user.getRoles()) {
                // Convert each UtbRole to a SimpleGrantedAuthority
                authorities.add(new SimpleGrantedAuthority(role.getName()));
                // Enumerating over authority a particular role has
                for (UtbAuthority authority : role.getAuthorities()) {
                    // Convert each UtbAuthority to a SimpleGrantedAuthority
                    authorities.add(new SimpleGrantedAuthority(authority.getName()));
                }
            }
            // Return a User object with username, password, and authorities
            return new User(user.getUsername(), user.getPassword(), authorities);
        } else {
            // If the user is not found, throw UsernameNotFoundException
            throw new UsernameNotFoundException(String.format("User by username %s not found", username));
        }
    }
}
