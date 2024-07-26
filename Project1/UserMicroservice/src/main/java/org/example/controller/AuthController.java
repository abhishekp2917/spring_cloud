package org.example.controller;

import org.example.exception.InvalidUsernameOrPasswordException;
import org.example.model.UtbUser;
import org.example.model.dto.SuccessResponseDTO;
import org.example.model.dto.UserDTO;
import org.example.service.UserServices;
import org.example.utility.DTOMapperUtil;
import org.example.utility.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for authentication-related operations.
 * Provides an endpoint for user login, handling authentication and returning appropriate responses.
 */
@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserServices userServices;  // Service for user-related database operations. Essential for fetching user details based on authentication.

    /**
     * Endpoint to handle user login requests.
     * This endpoint utilizes Spring Security's Authentication object to determine if the login attempt is successful.
     * It retrieves user details from the database and returns a success response if the user is authenticated.
     *
     * @param authentication Spring Security Authentication object containing authentication details.
     * @return A ResponseEntity with user details if authentication is successful,
     *         or throws InvalidUsernameOrPasswordException if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> login(Authentication authentication) {
        // Check if the Authentication object is not null and the user is authenticated.
        // If true, the user has successfully logged in.
        if (authentication != null && authentication.isAuthenticated()) {
            // Retrieve the username from the Authentication object.
            // This is the principal who has successfully authenticated.
            UtbUser user = userServices.findByUsername(authentication.getName());

            // Convert the UtbUser entity to a UserDTO for the response.
            // This ensures that only necessary user data is exposed in the response.
            UserDTO userDTO = DTOMapperUtil.toUserDTO(user);

            // Build and return a success response with HTTP status 200 (OK).
            // This indicates a successful login and includes the user details in the response body.
            return ResponseUtil.buildSuccessResponse(HttpStatus.OK, userDTO, "Login successful");
        } else {
            // If authentication fails or the Authentication object is null,
            // throw an InvalidUsernameOrPasswordException with an appropriate error message.
            // This exception will be handled globally to return an error response.
            throw new InvalidUsernameOrPasswordException("Login failed");
        }
    }
}
