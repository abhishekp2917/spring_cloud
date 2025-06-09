package org.example.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.example.annotation.ValidateRequest;
import org.example.dto.ErrorResponseDTO;
import org.example.dto.SuccessResponseDTO;
import org.example.exception.InternalServerErrorException;
import org.example.model.UtbRole;
import org.example.model.UtbUser;
import org.example.dto.UserDTO;
import org.example.service.RoleServices;
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
    private Environment environment;  // Provides access to application environment properties, such as server port, which can be useful for diagnostics and configuration.

    @Autowired
    private UserServices userServices;  // Service for user-related database operations. Essential for business logic and interaction with the database.

    @Autowired
    private RoleServices roleServices;  // Service for managing user roles. Necessary for validating and assigning user permissions.

    /**
     * Endpoint to check the status of the application.
     * This endpoint returns a simple "Active" message indicating that the application is running.
     * Useful for basic health checks or status monitoring.
     *
     * @return A status message indicating the application is active.
     */
    /**
     * @ApiOperation
     * Purpose: Describes a single API operation or endpoint in Swagger. It provides a summary and additional notes about what the operation does.
     * Use Cases: Use @ApiOperation to document the purpose of an endpoint, such as @GetMapping("/status") to indicate it returns the application status.
     *
     * @ApiResponses
     * Purpose: Documents the possible responses for an API operation, specifying the HTTP status codes and descriptions.
     * Use Cases: Use @ApiResponses to indicate expected outcomes of an endpoint, like @ApiResponse(code = 200, message = "OK") for a successful response or @ApiResponse(code = 500, message = "Internal Server Error") for error scenarios.
     */
    @ApiOperation(value = "Check Application Status", notes = "Returns 'Active' if the application is running")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
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
    @ApiOperation(value = "Retrieve Server Port", notes = "Returns the server port from the environment properties")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping("/env")
    public String getEnvironment() {
        return environment.getProperty("server.port");  // Fetches the server port from environment properties. If not used, there would be no easy way to retrieve port configuration from the application.
    }

    /**
     * Endpoint for user signup. This endpoint validates and processes user registration requests.
     * It encodes the user's password, manages user roles, and saves the user to the database.
     * Handles both success and failure cases and returns appropriate responses.
     *
     * @param user The user object containing user details, passed in the request body.
     * @return A ResponseEntity with a success message and user details if the operation is successful,
     *         or throws InternalServerErrorException if user creation fails.
     */
    @ApiOperation(value = "User Signup", notes = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully", response = SuccessResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorResponseDTO.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseDTO.class)
    })
    @PostMapping("/signup")
    @ValidateRequest  // Custom annotation used to validate the incoming request data before processing.
    public ResponseEntity<SuccessResponseDTO> signup(@RequestBody UtbUser user) {
        // Encode the user's password before saving it to the database to ensure passwords are stored securely.
        // Storing plaintext passwords would be a significant security risk.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Initialize a set to hold roles that are managed entities.
        Set<UtbRole> managedRoles = new HashSet<>();

        // Iterate over the roles provided by the user.
        // Fetch each role from the database to ensure it is a valid and managed entity.
        // Adding unvalidated roles would risk allowing unauthorized or non-existent permissions.
        for (UtbRole role : user.getRoles()) {
            UtbRole managedRole = roleServices.findById(role.getId());
            if (managedRole != null) {
                managedRoles.add(managedRole);
            }
        }
        user.setRoles(managedRoles);

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
