package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class to represent HTTP 401 Unauthorized errors.
 * This exception is used when a user fails to authenticate due to invalid token.
 *
 * The @ResponseStatus annotation maps this exception to an HTTP status code of 401 Unauthorized.
 * This informs the client that the request cannot be fulfilled due to authentication failure.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends AuthenticationException {

    /**
     * Constructor for InvalidTokenException with a custom message.
     * This allows the exception to carry a specific message that describes why the authentication failed.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     */
    public InvalidTokenException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidTokenException with a custom message and a cause.
     * This constructor allows the exception to be initialized with both a message and another throwable cause.
     * This is useful for exception chaining, where one exception is caused by another, allowing the propagation of the original cause.
     *
     * @param message The detail message that explains the reason for the exception. This message is included in the HTTP response.
     * @param cause   The cause of the exception (a throwable that caused this exception to be thrown). This allows tracking of the underlying issue.
     */
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
