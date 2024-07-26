package org.example.controller;

import org.example.annotation.ValidateRequest;
import org.example.model.UtbAuthority;
import org.example.model.dto.SuccessResponseDTO;
import org.example.exception.InternalServerErrorException;
import org.example.model.UtbUser;
import org.example.model.dto.UserDTO;
import org.example.service.AuthorityServices;
import org.example.service.UserServices;
import org.example.utility.DTOMapperUtil;
import org.example.utility.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller for managing user-related operations.
 * Provides endpoints to check application status, retrieve environment properties,
 * and handle user signup requests.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;  // Bean used for encoding user passwords. Essential for security to store passwords in a hashed format.

    @Autowired
    private Environment env;  // Provides access to application environment properties, such as server port, which can be useful for diagnostics and configuration.

    @Autowired
    private UserServices userServices;  // Service for user-related database operations. Essential for business logic and interaction with the database.

    @Autowired
    private AuthorityServices authorityServices;  // Service for managing user authorities. Necessary for validating and assigning user permissions.

    /**
     * Endpoint to check the status of the application.
     * This endpoint returns a simple "Active" message indicating that the application is running.
     * Useful for basic health checks or status monitoring.
     *
     * @return A status message indicating the application is active.
     */
    @GetMapping("/status")
    public String getStatus() {
        return "Active";  // Hardcoded status message. If this endpoint is not implemented, monitoring tools cannot confirm application health.
    }

    /**
     * Endpoint to retrieve the server port from the environment properties.
     * This can be useful for debugging or configuration purposes, especially in environments where the port may change.
     *
     * @return The server port configured in application properties.
     */
    @GetMapping("/env")
    public String getEnvironment() {
        return env.getProperty("server.port");  // Fetches the server port from environment properties. If not used, there would be no easy way to retrieve port configuration from the application.
    }

    /**
     * Endpoint for user signup. This endpoint validates and processes user registration requests.
     * It encodes the user's password, manages user authorities, and saves the user to the database.
     * Handles both success and failure cases and returns appropriate responses.
     *
     * @param user The user object containing user details, passed in the request body.
     * @return A ResponseEntity with a success message and user details if the operation is successful,
     *         or throws InternalServerErrorException if user creation fails.
     */
    @PostMapping("/signup")
    @ValidateRequest  // Custom annotation used to validate the incoming request data before processing.
    public ResponseEntity<SuccessResponseDTO> signup(@RequestBody UtbUser user) {
        // Encode the user's password before saving it to the database to ensure passwords are stored securely.
        // Storing plaintext passwords would be a significant security risk.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Initialize a set to hold authorities that are managed entities.
        Set<UtbAuthority> managedAuthorities = new HashSet<>();

        // Iterate over the authorities provided by the user.
        // Fetch each authority from the database to ensure it is a valid and managed entity.
        // Adding unvalidated authorities would risk allowing unauthorized or non-existent permissions.
        for (UtbAuthority authority : user.getAuthorities()) {
            UtbAuthority managedAuthority = authorityServices.findById(authority.getId());
            if (managedAuthority != null) {
                managedAuthorities.add(managedAuthority);
            }
        }
        user.setAuthorities(managedAuthorities);

        // Save the user to the database through the UserServices.
        // If the user is not saved correctly, the ID will be null.
        userServices.save(user);

        // Check if the user ID is set to verify successful user creation.
        // If the ID is null, it indicates a failure to save the user in the database, leading to an exception.
        if (user.getId() != null) {
            // Convert the user entity to a DTO for the response. This provides a cleaner representation of the user data.
            // Directly returning the entity could expose internal details.
            UserDTO userDTO = DTOMapperUtil.toUserDTO(user);
            // Build and return a success response with HTTP status 201 (Created).
            return ResponseUtil.buildSuccessResponse(HttpStatus.CREATED, userDTO, "User created successfully");
        } else {
            // If user creation failed, throw an InternalServerErrorException.
            // This exception is handled globally to ensure appropriate error responses are returned.
            throw new InternalServerErrorException("User couldn't be saved");
        }
    }
}
