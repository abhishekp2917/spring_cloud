package org.example.aspect;

import org.example.exception.BadRequestException;
import org.example.model.UtbRole;
import org.example.model.UtbUser;
import org.example.service.RoleServices;
import org.example.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aspect for validating user signup requests.
 *
 * This aspect uses Aspect-Oriented Programming (AOP) to enforce validation rules on user signup requests.
 * It intercepts method executions that are annotated with {@code @ValidateRequest} and applies validation logic
 * to ensure that user data meets specified criteria before the method is executed.
 *
 * The {@code @Before} annotation specifies that the {@code validateSignupUserRequest} method should be executed
 * before the {@code signup} method in {@code UserController}, provided that the method takes a {@code UtbUser} parameter.
 *
 * Validation checks performed include:
 * - Ensuring the username and password are not null or empty.
 * - Verifying that the user has at least one authority assigned.
 * - Checking that the provided authorities are valid and exist in the system.
 * - Confirming that the username does not already exist in the system.
 *
 * If any validation fails, a {@code BadRequestException} is thrown with an appropriate error message.
 */
@Aspect
@Component
public class RequestValidationAspect {

    @Autowired
    private RoleServices roleServices;

    @Autowired
    private UserServices userServices;

    /**
     * Validates the {@code UtbUser} object before the signup method is executed.
     *
     * This method performs several checks on the {@code UtbUser} object to ensure that:
     * - The username and password are provided and not empty.
     * - The user has at least one authority.
     * - The authorities are valid and exist in the system.
     * - The username is unique and not already taken.
     *
     * @param user the {@code UtbUser} object to validate
     * @throws BadRequestException if any validation check fails
     */
    @Before("@annotation(org.example.annotation.ValidateRequest) && execution(* org.example.controller.UserController.signup(..)) && args(user)")
    public void validateSignupUserRequest(UtbUser user) {
        boolean isValid = true;
        String errorMessage = "";
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            isValid = false;
            errorMessage = "Username must not be empty";
        }
        else if (user.getPassword() == null || user.getPassword().isEmpty()) {
            isValid = false;
            errorMessage = "Password must not be empty";
        }
        else if (user.getRoles() == null || user.getRoles().isEmpty()) {
            isValid = false;
            errorMessage = "User must have at least one role";
        }
        else if (!areValidAuthorities(user.getRoles())) {
            isValid = false;
            errorMessage = "Invalid authority supplied";
        }
        else if(userServices.findByUsername(user.getUsername()) != null) {
            isValid = false;
            errorMessage = "Username already exists";
        }
        if (!isValid) {
            throw new BadRequestException(errorMessage);
        }
    }

    /**
     * Checks if the provided roles are valid.
     *
     * This method verifies that all role IDs in the given set are present in the list of existing role IDs.
     *
     * @param roles the set of {@code UtbRole} to validate
     * @return {@code true} if all roles are valid, {@code false} otherwise
     */
    private boolean areValidAuthorities(Set<UtbRole> roles) {
        Set<Long> existingRolesId = roleServices.getRolesId();
        return existingRolesId
                .containsAll(
                        roles.stream()
                                .map(UtbRole::getId)
                                .collect(Collectors.toSet())
                );
    }
}
